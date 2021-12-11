package NIO.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(5005));
        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            long totalSize = 0;
            while (true) {
                int read = socketChannel.read(buffer);
                if(read == -1) {
                    break;
                }
                totalSize += read;
                buffer.clear();
            }
            System.out.println("receive: " + totalSize + " bytes");
        }
    }
}
