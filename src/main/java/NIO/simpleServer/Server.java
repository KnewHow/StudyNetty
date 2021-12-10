package NIO.simpleServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {
    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(5005));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            if(selector.selectNow() == 0) {
                //System.out.println("Server wait for 1s...");
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()) {
                SelectionKey key = iterator.next();
                System.out.println("Socket key hashCode: " + key.hashCode());
                if(key.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if(key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer  = (ByteBuffer) key.attachment();

                    byteBuffer.clear();
                    try {
                        socketChannel.read(byteBuffer);
                    } catch (Exception e){
                        System.out.println("client disconnect....");
                        key.cancel();
                        socketChannel.close();
                        iterator.remove();
                        continue;
                    }

                    byteBuffer.flip();
                    System.out.print("From client: ");
                    while (byteBuffer.hasRemaining()) {
                        System.out.print((char)byteBuffer.get());
                    }
                    System.out.println();
                    byteBuffer.clear();
                    String response = "Success";
                    byteBuffer.put(response.getBytes());
                    byteBuffer.flip();
                    try {
                        socketChannel.write(byteBuffer);
                    } catch (Exception e) {
                        System.out.println("client disconnect....");
                        key.cancel();
                        socketChannel.close();
                        iterator.remove();
                        continue;
                    }


                }
                iterator.remove();
            }
        }
    }
}
