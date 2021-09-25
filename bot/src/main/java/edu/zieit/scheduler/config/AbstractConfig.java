package edu.zieit.scheduler.config;

import napi.configurate.yaml.conf.Configuration;
import napi.configurate.yaml.conf.ConfigurationBuilder;
import napi.configurate.yaml.source.ConfigSources;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractConfig {

    protected final Configuration conf;

    public AbstractConfig(Path rootDir, String configPath, Map<Class<?>, TypeSerializer> serializers) {
        ConfigurationBuilder builder = Configuration.builder();

        builder.source(ConfigSources.resource(configPath, this)
                        .copyTo(rootDir));

        for (var entry : serializers.entrySet()) {
            builder.serializer(entry.getKey(), entry.getValue());
        }

        this.conf = builder.build();
    }

    public void reload() throws IOException {
        conf.reload();

        try {
            load();
        } catch (ObjectMappingException e) {
            throw new IOException(e);
        }
    }

    protected abstract void load() throws ObjectMappingException;

    protected Map<String, String> loadProperties(ConfigurationNode node) {
        Map<String, String> map = new HashMap<>();
        for (var entry : node.getChildrenMap().entrySet()) {
            map.put(entry.getKey().toString(), entry.getValue().getString());
        }
        return map;
    }

}
