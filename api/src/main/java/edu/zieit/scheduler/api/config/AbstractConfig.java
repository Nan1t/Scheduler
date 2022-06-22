package edu.zieit.scheduler.api.config;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.function.Consumer;

public abstract class AbstractConfig {

    private final YamlConfigurationLoader loader;
    protected ConfigurationNode conf;

    public AbstractConfig(Path rootDir, String configPath) {
//        ConfigurationOptions options = ConfigurationOptions.defaults()
//                .serializers(serializers());

        this.loader = YamlConfigurationLoader.builder()
                .path(resolveFile(rootDir, configPath))
                //.defaultOptions(options)
                .defaultOptions(opts -> opts.serializers(this::serializers))
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .build();
    }

    public void reload() throws IOException {
        conf = loader.load();
        load();
    }

    public void save() throws IOException {
        loader.save(conf);
    }

    protected abstract void load() throws SerializationException;

    protected void serializers(TypeSerializerCollection.Builder build) {
        // Ignore
    }

    protected Properties loadProperties(ConfigurationNode node) {
        Properties properties = new Properties();
        for (var entry : node.childrenMap().entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().getString();
            properties.put(key, value);
        }
        return properties;
    }

    private Path resolveFile(Path rootDir, String configPath) {
        Path file = Paths.get(rootDir.toString(), configPath);

        if (!Files.exists(file)) {
            InputStream in = getClass().getResourceAsStream(configPath);

            try {
                Files.copy(in, file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return file;
    }

}
