package edu.zieit.scheduler.schedule.teacher;

import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.schedule.AbstractScheduleInfo;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TeacherScheduleInfo extends AbstractScheduleInfo {

    private final Map<String, NamespacedKey> associations;

    public TeacherScheduleInfo(URL url, Map<String, NamespacedKey> associations) {
        super(url);
        this.associations = associations;
    }

    public NamespacedKey getAssociation(String abbreviation) {
        return associations.get(abbreviation.toLowerCase());
    }

    public static class Serializer implements TypeSerializer<TeacherScheduleInfo> {

        @Override
        public TeacherScheduleInfo deserialize(Type type, ConfigurationNode node) throws SerializationException {
            URL url;

            try {
                url = new URL(node.node("url").getString(""));
            } catch (MalformedURLException e) {
                throw new SerializationException("Incorrect or missing schedule URL");
            }

            Map<String, NamespacedKey> associations = new HashMap<>();

            for (var entry : node.node("associations").childrenMap().entrySet()) {
                String abbreviation = entry.getKey().toString().toLowerCase();
                String rawKey = entry.getValue().getString(null);
                NamespacedKey key = NamespacedKey.parse(rawKey);
                associations.put(abbreviation, key);
            }

            return new TeacherScheduleInfo(url, associations);
        }

        @Override
        public void serialize(Type type, @Nullable TeacherScheduleInfo obj, ConfigurationNode node) throws SerializationException {

        }
    }
}
