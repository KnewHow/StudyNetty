package BIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    private static final Logger LOGGER = LogManager.getLogger(BIOServer.class.getName());

    public static void main(String[] args) throws  Exception{
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket server = new ServerSocket(5006);
        LOGGER.error("[{}]Server start...", Thread.currentThread().getId());
        while (true) {
            final Socket socket = server.accept();
            LOGGER.info("new connect arrive");
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    handler(socket);
                }
            });
        }
    }

    public static void handler(final Socket socket) {
        DataOutputStream dataOutputStream = null;
        try {
            byte[] buffer = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                int read = inputStream.read(buffer);
                if(read != -1) {
                    stringBuilder.append(new String(buffer, 0, read));
                } else {
                    break;
                }
            }
            LOGGER.info("get message from client: " + stringBuilder.toString());
            OutputStream outputStream = socket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF("Success");
            dataOutputStream.flush();
        } catch (IOException e) {
            LOGGER.error("Get inputStream error:" + e.getMessage());
        } finally {
            try {
                socket.close();
                dataOutputStream.close();
            } catch (IOException e) {
                LOGGER.error("close socket error:" + e.getMessage());
            }
        }

    }
}
