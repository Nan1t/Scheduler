import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;


public class TestDao {

    private byte[] genBytes() {
        byte[] bytes = new byte[200];
        ThreadLocalRandom.current().nextBytes(bytes);
        return bytes;
    }

    @Test
    public void testBase64() throws Exception {
        long time = System.nanoTime();
        // buf = ByteBuffer.allocate();
        //System.out.println(Base64.getEncoder().encodeToString(bytes));
        System.out.println(System.nanoTime() - time);
    }

    @Test
    public void testJson() throws Exception {
        byte[] bytes = genBytes();
        long time = System.nanoTime();
        // Code
        System.out.println(System.nanoTime() - time);
    }

    @Test
    public void testRaw() throws Exception {
        byte[] bytes = genBytes();
        long time = System.nanoTime();
        // Code
        System.out.println(System.nanoTime() - time);
    }

}
