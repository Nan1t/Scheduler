package tests;

import edu.zieit.scheduler.config.Configuration;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class TestConfiguration {

    @Test
    public void testLoadFromResource() throws Exception {
        Configuration conf = new Configuration("/test.yml", this);
        conf.load();

        System.out.printf("Name: %s%n", conf.node("test", "name").getString());
        System.out.printf("Age: %s%n", conf.node("test", "age").getInt());
    }

    @Test
    public void testLoadFromFile() throws Exception {
        Configuration conf = new Configuration(Paths.get("./test.yml"));
        conf.load();

        System.out.printf("Name: %s%n", conf.node("test", "name").getString());
        System.out.printf("Age: %s%n", conf.node("test", "age").getInt());
    }

}
