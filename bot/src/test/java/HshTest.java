import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.lang.reflect.Type;
import java.nio.file.Paths;

public class HshTest {

    @Test
    public void test() throws Exception {
        var conf = YamlConfigurationLoader.builder()
                .path(Paths.get("D:\\IdeaProjects\\Scheduler\\bot\\build\\libs\\test.yml"))
                .defaultOptions(opts -> opts.serializers(build -> {
                    build.register(User.class, new UserSerializer());
                }))
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .build()
                .load();

        System.out.println(conf.node("users").getList(User.class));
    }

    private static class User {
        String name;
        int age;
    }

    private static class UserSerializer implements TypeSerializer<User> {
        @Override
        public User deserialize(Type type, ConfigurationNode node) throws SerializationException {
            User user = new User();
            user.name = node.node("name").getString();
            user.age = node.node("age").getInt();
            return user;
        }

        @Override
        public void serialize(Type type, @Nullable User obj, ConfigurationNode node) throws SerializationException {

        }
    }

}
