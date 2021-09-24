package edu.zieit.scheduler.schedule.teacher;

import com.google.common.reflect.TypeToken;
import edu.zieit.scheduler.api.NamespaceKey;
import edu.zieit.scheduler.schedule.AbstractScheduleInfo;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TeacherScheduleInfo extends AbstractScheduleInfo {

    private final Map<String, NamespaceKey> associations;

    public TeacherScheduleInfo(URL url, Map<String, NamespaceKey> associations) {
        super(url);
        this.associations = associations;
    }

    public NamespaceKey getAssociation(String abbreviation) {
        return associations.get(abbreviation.toLowerCase());
    }

    public static class Serializer implements TypeSerializer<TeacherScheduleInfo> {

        @Override
        public TeacherScheduleInfo deserialize(TypeToken<?> type, ConfigurationNode node)
                throws ObjectMappingException {
            URL url;

            try {
                url = new URL(node.getNode("url").getString(""));
            } catch (MalformedURLException e) {
                throw new ObjectMappingException("Incorrect or missing schedule URL");
            }

            Map<String, NamespaceKey> associations = new HashMap<>();

            for (var entry : node.getNode("associations").getChildrenMap().entrySet()) {
                String abbreviation = entry.getKey().toString().toLowerCase();
                String rawKey = entry.getValue().getString(null);
                NamespaceKey key = NamespaceKey.parse(rawKey);
                associations.put(abbreviation, key);
            }

            return new TeacherScheduleInfo(url, associations);
        }

        @Override
        public void serialize(TypeToken<?> type, TeacherScheduleInfo obj, ConfigurationNode value) {

        }
    }
}
