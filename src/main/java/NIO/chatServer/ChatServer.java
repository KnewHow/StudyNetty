package NIO.chatServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class ChatServer {
    private static final int PORT = 5005;
    private ServerSocketChannel serverSocketChannel = null;
    private Selector selector = null;

    public ChatServer() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void start() {
        try {
            while (true) {
                if(selector.selectNow() <= 0) {
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if(key.isAcceptable()) {
                        SocketChannel channel = (SocketChannel) serverSocketChannel.accept();
                        System.out.println("new person join group: " + channel.getRemoteAddress());
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                    } else if(key.isReadable()) {
                        readMessage(key);
                    }
                    iterator.remove();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessage(SelectionKey key) {
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int count = channel.read(byteBuffer);
            if(count > 0) {
                byteBuffer.flip();
                String msg = new String(byteBuffer.array());
                System.out.println("Receive msg from client:["+channel.getRemoteAddress()+"]: " + msg);
                sendMessageToOther("["+channel.getRemoteAddress()+"]: " + msg, key);
            } else if(count == -1) {
                System.out.println(channel.getRemoteAddress() + " leave group...");
                key.cancel();
                channel.close();
            }
        } catch (IOException e) {
            if(channel != null) {
                try {
                    System.out.println(channel.getRemoteAddress() + "leave group...");
                    key.cancel();
                    channel.close();
                } catch (IOException ex) {
                        ex.printStackTrace();
                }
            }
        }

    }

    private void sendMessageToOther(String msg, SelectionKey key) throws IOException {
        for(SelectionKey otherKey: selector.keys()) {
            SelectableChannel channel = otherKey.channel();
            if(!(channel instanceof SocketChannel) || otherKey == key) {
                continue;
            }
            SocketChannel otherChannel = (SocketChannel) otherKey.channel();
            otherChannel.write(ByteBuffer.wrap(msg.getBytes()));
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }

}
