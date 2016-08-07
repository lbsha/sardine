package sardine.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import sardine.SardineBase;
import sardine.asset.AssetsHandler;
import sardine.monitor.Metrics;
import sardine.utils.StopWatch;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;

/**
 * @author bruce_sha
 *         2015/6/11
 * @since 1.0.0
 */
@ChannelHandler.Sharable
public class SardineInboundHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    final MatcherProcessor processor;

    public SardineInboundHandler(MatcherProcessor processor) {
        this.processor = processor;
    }

    protected void messageReceived(final ChannelHandlerContext ctx, final FullHttpRequest request) throws Exception {

        final FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), OK);

        {//TODO 临时解决方案，待转移到sardineRequest
            String clientIP = request.headers().getAndConvert("X-Forwarded-For");
            if (clientIP == null) {
                InetSocketAddress is = (InetSocketAddress) ctx.channel().remoteAddress();
                clientIP = is.getAddress().getHostAddress();
            } else {
                clientIP = clientIP.split(",")[0].trim();
            }
            request.headers().set("X-SARDINE-IP", clientIP);
        }

        final StopWatch stopWatch = StopWatch.newInstance();

        try {
            processor.process(request, response, ctx);
        } catch (NotConsumedException ignore) {
            try {
                if (AssetsHandler.readResource(request, ctx)) {
                    Metrics.assertsRequestCount();
                } else {
                    // TODO : Not use an exception in order to be faster.
                    // TODO:注意这里有问题，到底先处理静态文件还是动态文件
                    response.setStatus(HttpResponseStatus.NOT_FOUND);
                    response.content().writeBytes(SardineBase.NOT_FOUND.getBytes(StandardCharsets.UTF_8));
                    final int readableBytes = response.content().readableBytes();
                    response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, readableBytes);
                    ctx.writeAndFlush(response);

                    // Metrics.responseBytesWithOutAsserts(readableBytes);
                }
            } catch (Exception e) {
                //ignore
            }
        } finally {
            Metrics.totalRequestCount();
            Metrics.totalResponseTime(stopWatch.nanoSeconds());
            Metrics.responseXxxCount(response.status());
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}