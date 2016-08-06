package sardine.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.NetUtil;
import sardine.asset.AssetsHandler;
import sardine.log.Logs;

import java.util.Objects;
import java.util.Optional;

/**
 * sardine服务：与netty耦合的集中处
 *
 * @auth bruce-sha
 * @date 2015/5/21
 */
public class SardineServer {

    final private ChannelHandler handler;
    private EventLoopGroup workerGroup;
    private EventLoopGroup bossGroup;

    /*public*/ SardineServer(ChannelHandler handler) {
        this.handler = Objects.requireNonNull(handler);
    }

    /**
     * 点火：启动 server
     *
     * @param host
     * @param port
     * @param staticFilesFolder
     * @param externalFilesFolder
     */
    public void fire(final String host,
                     final int port,
                     final Optional<String> staticFilesFolder,
                     final Optional<String> externalFilesFolder) {

        if (staticFilesFolder.isPresent()) AssetsHandler.staticFileFolder(staticFilesFolder.get());
        if (externalFilesFolder.isPresent()) AssetsHandler.externalStaticFileFolder(externalFilesFolder.get());

        try {
            workerGroup = new NioEventLoopGroup();
            bossGroup = new NioEventLoopGroup();

            final ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
                            // pipeline.addLast(new HttpContentCompressor()); // 不能针对 chunked
                            // pipeline.addLast(new HttpChunkContentCompressor()); // 全部压缩
                            pipeline.addLast(new ChunkedWriteHandler());
                            pipeline.addLast(handler);
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, NetUtil.SOMAXCONN)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            Channel channel = bootstrap.bind(host, port).sync().channel();

            Logs.log("service started on port {}", port);

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            Logs.error(e);
            Thread.currentThread().interrupt();
        } finally {
            stop();
        }
    }

    //http://netty.io/wiki/user-guide-for-5.x.html#wiki-h3-17
    public void stop() {

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

//        logger.info(">>> {} shutting down ...", NAME);
//        try {
//            if (server != null) {
//                server.stop();
//            }
//        } catch (Exception e) {
//            logger.error("stop failed",e);
//            System.exit(100); // NOSONAR
//        }
//        logger.info("done");
    }

}
