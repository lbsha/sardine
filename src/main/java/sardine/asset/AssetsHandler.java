package sardine.asset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedStream;
import sardine.log.Logs;
import sardine.monitor.Metrics;
import sardine.utils.Mimes;
import sardine.utils.Utils;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static io.netty.handler.codec.http.HttpHeaderUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static java.util.Collections.synchronizedMap;

/**
 * 静态文件处理器
 *
 * @auth bruce-sha
 * @date 2015/5/24
 */
public class AssetsHandler {

    private static String staticFileFolder;
    private static String externalStaticFileFolder;

    final static Map<String, ByteBuf> cache = synchronizedMap(new SimpleLRUCache<String, ByteBuf>(1024));

    public static void staticFileFolder(final String staticFileLocation) {
        staticFileFolder = Objects.requireNonNull(staticFileLocation);
    }

    public static void externalStaticFileFolder(final String externalStaticFileLocation) {
        externalStaticFileFolder = Objects.requireNonNull(externalStaticFileLocation);
    }

    public static boolean readResource(final FullHttpRequest request, final ChannelHandlerContext ctx) {

        final String path = Utils.path(request);
        final String contentType = Mimes.contentType(path);

        ByteBuf byteBuf = null;

        if (cache.containsKey(path)) {
            byteBuf = cache.get(path);
            byteBuf.resetReaderIndex();
            byteBuf.retain();
        } else {
            try {
                byteBuf = readResource(path);
                if (byteBuf != null) cache.putIfAbsent(path, byteBuf);
            } catch (Exception e) {
                Logs.error("read file error: ", e);
            }
        }

        if (byteBuf != null) {

            Metrics.assertsResponseBytes(byteBuf.readableBytes());

            // 2048 字节
            if (byteBuf.readableBytes() < 1 << 11) {

                DefaultFullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), OK, byteBuf);

                response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType + "; charset=" + StandardCharsets.UTF_8);
                if (isKeepAlive(request))
                    response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                response.headers().setLong(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

                ChannelFuture lastContentFuture = ctx.writeAndFlush(response);

//                headers.setLong(HttpHeaderNames.CONTENT_LENGTH, bytes);
//
//                ctx.write(response);
//                ctx.writeAndFlush(byteBuf, ctx.newProgressivePromise());

//                ChannelFuture lastContentFuture = ctx.writeAndFlush(byteBuf);//ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
//                ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
                if (!isKeepAlive(request)) lastContentFuture.addListener(ChannelFutureListener.CLOSE);
            } else {
                // 大文件用 chunked 方式

                final DefaultHttpResponse response = new DefaultHttpResponse(request.protocolVersion(), OK);

                response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType + "; charset=" + StandardCharsets.UTF_8);
                if (isKeepAlive(request))
                    response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);

                ctx.write(response);
                ctx.write(byteBuf);
                ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
                //主动关闭连接 即不keep-alive
                if (!isKeepAlive(request)) lastContentFuture.addListener(ChannelFutureListener.CLOSE);
            }

            return true;
        }

        // 不是静态文件
        return false;
    }

    private static ByteBuf readResource(final String path) {

        ByteBuf buffer = Unpooled.buffer();

        // ClassLoader目录下
        if (!buffer.isReadable() && staticFileFolder != null) {

            final String filePath = staticFileFolder + path.substring(1);
            final URL url = Thread.currentThread().getContextClassLoader().getResource(filePath);

            InputStream inputStream = null;
            // 考虑安全性 不允许显示目录内容
            if (url != null && "jar".equals(url.getProtocol())) {
                try {
                    URLConnection urlConnection = url.openConnection();
                    JarURLConnection jarUrlConnection = (JarURLConnection) urlConnection;
                    JarEntry jarEntry = jarUrlConnection.getJarEntry();
                    JarFile jarFile = jarUrlConnection.getJarFile();
                    // check if this is not a folder
                    // we can't rely on jarEntry.isDirectory() because of http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6233323
                    if (!jarEntry.isDirectory() && jarFile.getInputStream(jarEntry) != null) {
                        inputStream = urlConnection.getInputStream();
                        buffer.writeBytes(inputStream, inputStream.available());
                    }
                } catch (IOException e) {
                    // ignore
                } finally {
                    closeQuietly(inputStream);
                }
            }

            if (url != null && "file".equals(url.getProtocol())) {
                try {
                    final File file = new File(url.toURI());
                    if (file.exists() && file.isFile() && file.canRead()) {
                        inputStream = url.openConnection().getInputStream();
                        buffer.writeBytes(inputStream, inputStream.available());
                    }
                } catch (URISyntaxException | IOException e) {
                    //ignore
                } finally {
                    closeQuietly(inputStream);
                }
            }
        }

        // ClassLoader之外目录下
        FileInputStream fileInputStream = null;
        if (!buffer.isReadable() && externalStaticFileFolder != null) {
            String filePath = externalStaticFileFolder + path;
            try {
                File file = new File(filePath);
                if (file.exists() && file.isFile() && file.canRead()) {
                    fileInputStream = new FileInputStream(file);
                    buffer.writeBytes(fileInputStream, fileInputStream.available());
                }
            } catch (IOException e) {
                //ignore
            } finally {
                closeQuietly(fileInputStream);
            }
        }

        if (!buffer.isReadable()) return null;
        else return buffer.retain();//buffer.retain(1<<10);
    }

    static void d() {

    }

    static void closeQuietly(final InputStream inputStream) {
        try {
            if (inputStream != null) inputStream.close();
        } catch (Exception e) {
            // ignore
        }
    }

    private void readddd() {

    }

    static ByteBuf buffer = null;

    public static boolean handleResource2(final FullHttpRequest request, final ChannelHandlerContext ctx) {
        try {
            final String path = Utils.path(request);
            final String contentType = Mimes.contentType(path);

            final HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), OK);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType + "; charset=UTF-8");
            if (isKeepAlive(request)) response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

            boolean handled = false;

            if (!handled && staticFileFolder != null) {
                InputStream inputStream = null;

                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                String filePath = staticFileFolder + path.substring(1);

                URL url = classLoader.getResource(filePath);

                //考虑安全性 不允许显示目录内容
                if ("jar".equals(url.getProtocol())) {
                    URLConnection urlConnection = url.openConnection();
                    JarURLConnection jarUrlConnection = (JarURLConnection) urlConnection;
                    JarEntry jarEntry = jarUrlConnection.getJarEntry();
                    JarFile jarFile = jarUrlConnection.getJarFile();
                    // check if this is not a folder
                    // we can't rely on jarEntry.isDirectory() because of http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6233323
                    if (!jarEntry.isDirectory() && jarFile.getInputStream(jarEntry) != null) {
                        inputStream = urlConnection.getInputStream();
                    }
                }
//File f=null;
                if ("file".equals(url.getProtocol())) {
                    final File file = new File(url.toURI());
//                    f=file;
                    if (file.exists() && file.isFile() && file.canRead())
                        inputStream = url.openConnection().getInputStream();
                }


                if (inputStream != null) {
                    response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
                    {
                        //这个要缓存住
                        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
                        long startTime = bean.getStartTime();
                        long expiresTime = startTime + 1000 * 24 * 60 * 60;

                        Date expiresDate = new Date();

                    }
                    ctx.write(response);

//                    ctx.write(new ChunkedStream(inputStream));
//if(buffer==null) {
//                   /* ByteBuf */
//    buffer = Unpooled.buffer(128); // see http://stackoverflow.com/a/15088238/834
//    buffer.writeBytes(inputStream, inputStream.available());
//    buffer.retain(1000);
//}else{
//    buffer.resetReaderIndex();
//    buffer.retain();
////    buffer.refCnt();//TODO
//}
//                    ChannelFuture future =    ctx.write(buffer);

//                    buffer.resetReaderIndex();
//                    buffer.resetWriterIndex();
//                    ChannelFuture future = ctx.write(new ChunkedStream(inputStream),ctx.newProgressivePromise());
                    ChannelFuture future = ctx.write(new ChunkedStream(inputStream));
                    if (!isKeepAlive(request)) future.addListener(ChannelFutureListener.CLOSE);

                    //主动关闭连接 即不keep-alive
//                    future.addListener(new ChannelFutureListener() {
//                        @Override
//                        public void operationComplete(ChannelFuture f) throws Exception {
//                            f.channel().close();
//                        }
//                    });

//                    ctx.write(new ChunkedNioFile(f) );
                    handled = true;
                }
            }

            if (!handled && externalStaticFileFolder != null) {
                File file = new File(externalStaticFileFolder + path);
                if (file.exists() && file.isFile() && file.canRead()) {
                    RandomAccessFile raf = new RandomAccessFile(file, "r");
                    long length = raf.length();
                    response.headers().setLong(HttpHeaderNames.CONTENT_LENGTH, length);
                    {
                        //缓存和过期时间设置
                        long lastModifiedTime = file.lastModified();

                        response.headers();

                    }
                    ctx.write(response);
                    ctx.write(new DefaultFileRegion(raf.getChannel(), 0, length), ctx.newProgressivePromise());
                    handled = true;
                }
            }

            if (!handled) return false;

            ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!isKeepAlive(request)) lastContentFuture.addListener(ChannelFutureListener.CLOSE);

        } catch (Exception e) {
            Logs.error("server static file error: ", e);
            return false;
        }
        return true;
    }
}
