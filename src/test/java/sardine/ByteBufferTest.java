package sardine;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author bruce-sha
 *   2015/6/19
 */
public class ByteBufferTest {
    public static void main(String[] args) {

        //create a ByteBuf of capacity is 16
        ByteBuf buf = Unpooled.buffer(16);
        //write data to buf
        for (int i = 0; i < 16; i++) {
            buf.writeByte(i + 1);
        }
        //read data from buf
        for (int i = 0; i < buf.capacity(); i++) {
            System.out.print(buf.getByte(i));
        }
        System.out.println();
        while (buf.isReadable()) {
            System.out.print(buf.readByte());
        }
        buf.retain();
        buf.release();
        buf.retain(1);
        System.out.println();
        buf.resetReaderIndex();
        while (buf.isReadable()) {
            System.out.print(buf.readByte());
        }


//        ByteBuf buffer = Unpooled.buffer(128);
//
//        buffer.writeBytes("abcd1234".getBytes());
//
////        buffer.re
//
//        System.out.println(buffer.toString(StandardCharsets.UTF_8));
//        System.out.println(buffer.toString(StandardCharsets.UTF_8));
    }
}
