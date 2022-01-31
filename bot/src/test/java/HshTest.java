import edu.zieit.scheduler.api.util.HashUtil;
import org.junit.jupiter.api.Test;

import java.net.URL;

public class HshTest {

    @Test
    public void test() throws Exception {
        URL url = new URL("https://www.zieit.edu.ua/wp-content/uploads/Rozklad/1k.xls");
        System.out.println(HashUtil.getHash(url));
    }

}
