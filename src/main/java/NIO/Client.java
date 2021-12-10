package NIO;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 5005);
        if(!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("Do other thing you want before connect server");
            }
        }

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        for(int i = 0; i < 100; ++i) {
            long begin = System.currentTimeMillis();
            String str = "I am " + i +" th a message from client. ^.^";
            buffer.clear();
            buffer.put(str.getBytes());
            buffer.flip();
            socketChannel.write(buffer);

            buffer.clear();
            socketChannel.read(buffer);
            buffer.flip();
            long end = System.currentTimeMillis();
            System.out.println("["+ i +"] response: " + new String(buffer.array(), 0, buffer.limit()) + ", took: " + (end - begin) + " ms");
            Thread.sleep(10);
        }
    }
}
