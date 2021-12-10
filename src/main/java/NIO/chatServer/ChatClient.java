package NIO.chatServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class ChatClient {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 5005;
    private SocketChannel socketChannel = null;
    private Selector selector = null;
    private String username;

    public ChatClient() throws IOException {
        socketChannel = SocketChannel.open((new InetSocketAddress(HOST, PORT)));
        socketChannel.configureBlocking(false);
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString();
        System.out.println("current user: " + username);
    }

    public void sendMessage(String msg) {
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            System.err.println("Send message failure!");
            e.printStackTrace();
        }
    }

    public void listenOthersMessage() {
        while (true) {
           try {
               int count = selector.select(100);
               if(count <= 0){
                   continue;
               }
               Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
               while (iterator.hasNext()) {
                   SelectionKey key = iterator.next();
                   if(key.isReadable()) {
                       SocketChannel channel = (SocketChannel) key.channel();
                       ByteBuffer buffer = ByteBuffer.allocate(1024);
                       channel.read(buffer);
                       buffer.flip();
                       String message = new String(buffer.array());
                       System.out.println(message);
                   }
                   iterator.remove();
               }
           } catch (IOException e) {
               System.err.println("listen other message failure!");
                e.printStackTrace();
           }
        }
    }

    public static void main(String[] args) throws IOException {
        ChatClient chatClient = new ChatClient();
        new Thread() {
            public void run(){
                chatClient.listenOthersMessage();
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String s = scanner.next();
            chatClient.sendMessage(s);
        }
    }
}
