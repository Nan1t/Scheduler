package edu.zieit.scheduler.api.config;

import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Language extends AbstractConfig {

    private final Map<String, String> fields = new HashMap<>();
    private final Map<String, List<String>> lists = new HashMap<>();

    public Language(Path rootDir, String configPath) {
        this(rootDir, configPath, Map.of());
    }

    public Language(Path rootDir, String configPath, Map<Class<?>, TypeSerializer<?>> serializers) {
        super(rootDir, configPath, serializers);
    }

    public String of(String key) {
        return fields.getOrDefault(key, key);
    }

    public List<String> ofList(String key) {
        return lists.getOrDefault(key, Collections.singletonList(key));
    }

    @Override
    protected void load() throws SerializationException {
        for (var entry : conf.childrenMap().entrySet()) {
            String key = entry.getKey().toString();

            if (entry.getValue().isList()) {
                lists.put(key, entry.getValue().getList(String.class));
            } else {
                fields.put(key, entry.getValue().getString());
            }
        }
    }
}
