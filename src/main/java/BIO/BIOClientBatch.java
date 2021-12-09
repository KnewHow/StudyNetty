package BIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BIOClientBatch {
    private static final Logger LOGGER = LogManager.getLogger(BIOClientBatch.class.getName());
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        for(int i = 0; i < 2000; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    client();
                }
            });
        }
        executor.shutdown();
        boolean r =executor.awaitTermination(1, TimeUnit.MINUTES);
    }

    public static void client() {
        try {
            long begin = System.currentTimeMillis();
            //Socket socket = new Socket("123.60.46.246", 5006);
            Socket socket = new Socket("127.0.01", 5006);
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
            LOGGER.info("receive server message: " + stringBuilder.toString() +", took: " + (end - begin) + " ms");
            dataOutputStream.close();
            socket.close();
        } catch (Exception e) {
            LOGGER.error("Send message to client error: " + e.getMessage());
        }

    }
}
