import edu.zieit.scheduler.api.util.HashUtil;
import edu.zieit.scheduler.util.BCrypt;
import org.junit.jupiter.api.Test;

import java.net.URL;

public class HshTest {

    @Test
    public void test() throws Exception {
        String salt = BCrypt.gensalt();

        System.out.println("Salt: '"+salt+"'");
        System.out.println("Password: " + BCrypt.hashpw("user", salt));
    }

}
