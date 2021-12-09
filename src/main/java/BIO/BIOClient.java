package BIO;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class BIOClient {
    public static void main(String[] args) throws Exception {
        long begin = System.currentTimeMillis();
        Socket socket = new Socket("127.0.0.1", 5006);
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeUTF("I am request from client");
        dataOutputStream.flush();
        socket.shutdownOutput();

        InputStream inputStream = socket.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        byte[] buffer = new byte[1024];
        while (true) {
            int read = inputStream.read(buffer);
            if(read != -1) {
                stringBuilder.append(new String(buffer, 0, read));
            } else {
                break;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("receive server message: " + stringBuilder.toString() +", took: " + (end - begin) + " ms");
    }
}
