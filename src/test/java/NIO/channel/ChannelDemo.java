package NIO.channel;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChannelDemo {

    @Test
    public void writeFile() throws Exception {
        String str = "Hello, 袁国浩";
        FileOutputStream fileOutputStream = new FileOutputStream("/tmp/test.txt");
        FileChannel channel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes());
        byteBuffer.flip();
        channel.write(byteBuffer);
        fileOutputStream.close();
    }

    @Test
    public void readFile() throws  Exception{
        File file = new File("/tmp/test.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());
        channel.read(byteBuffer);
        byteBuffer.flip();
        String str = new String(byteBuffer.array());
        System.out.println(str);
        fileInputStream.close();
    }

    @Test
    public void copyFile() throws Exception{
        File file = new File("/tmp/test.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        FileOutputStream fileOutputStream = new FileOutputStream("/tmp/test.bak.txt");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        while (true) {
            byteBuffer.clear();
            int read = channel.read(byteBuffer);
            if(read == -1) {
                break;
            }
            byteBuffer.flip();
            outputStreamChannel.write(byteBuffer);
        }

        fileOutputStream.close();
        fileInputStream.close();
    }

    @Test
    public void copyFileWithTransferFrom() throws Exception{
        File file = new File("/tmp/test.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("/tmp/test.bak.txt");
        FileChannel outputStreamChannel = fileOutputStream.getChannel();
        outputStreamChannel.transferFrom(channel, 0, channel.size());

        fileOutputStream.close();
        fileInputStream.close();
    }
}
