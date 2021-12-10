package NIO.buffer;

import org.junit.Test;

import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedByteBufferTest {
    @Test
    public void bufferTest() {
        IntBuffer intBuffer = IntBuffer.allocate(5);
        intBuffer.flip();
        intBuffer.clear();
        intBuffer.isReadOnly();
    }

    @Test
    public void modifyFile() throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("/tmp/test.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        buffer.put(0, (byte) 'Y');
        buffer.put(4, (byte) 'G');
        randomAccessFile.close();
        System.out.println("success....");


    }
}
