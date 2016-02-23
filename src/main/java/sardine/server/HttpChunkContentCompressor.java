package sardine.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.HttpContentCompressor;

/**
 * @auth bruce-sha
 * @date 2015/6/15
 */
public final class HttpChunkContentCompressor extends HttpContentCompressor {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        /*
          convert ByteBuf to HttpContent to make it work with compression.
          This is needed as we use the ChunkedWriteHandler to send files when compression is enabled.
         */
        if (msg instanceof ByteBuf) msg = new DefaultHttpContent((ByteBuf) msg);

        super.write(ctx, msg, promise);
    }
}