package NIO.zerocopy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open((new InetSocketAddress("127.0.0.1", 5005)));
        FileInputStream inputStream = new FileInputStream("/tmp/book.pdf");
        FileChannel fileChannel = inputStream.getChannel();
        long begin =System.currentTimeMillis();
        fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("took: " + (System.currentTimeMillis() - begin) + " ms");
        socketChannel.close();
        inputStream.close();

    }
}
