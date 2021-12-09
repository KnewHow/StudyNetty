package BIO;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class GenerateTestBash {


    @Test
    public void generateTestBash() throws Exception {
        PrintWriter writer = new PrintWriter("/home/knewhow/dev/CPPSocketLearning/build/test.sh", "UTF-8");
        writer.println("rm -f /tmp/study-netty/cpp-test.log ");
        for(int i = 0; i < 1000; i++) {
            writer.println("./client 123.60.46.246 5005 >> /tmp/study-netty/cpp-test.log 2>&1 &");
        }
        writer.flush();
        writer.close();
    }
}
