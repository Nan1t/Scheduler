package edu.zieit.scheduler.schedule.teacher;

import com.google.common.reflect.TypeToken;
import edu.zieit.scheduler.schedule.AbstractScheduleInfo;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TeacherScheduleInfo extends AbstractScheduleInfo {

    private final Map<String, String> associations;

    public TeacherScheduleInfo(URL url, Map<String, String> associations) {
        super(url);
        this.associations = associations;
    }

    public String getScheduleId(String abbreviation) {
        return associations.get(abbreviation);
    }

    public static class Serializer implements TypeSerializer<TeacherScheduleInfo> {

        @Override
        public @Nullable TeacherScheduleInfo deserialize(TypeToken<?> type, ConfigurationNode node)
                throws ObjectMappingException {
            URL url;

            try {
                url = new URL(node.getNode("url").getString(""));
            } catch (MalformedURLException e) {
                throw new ObjectMappingException("Incorrect or missing schedule URL");
            }

            Map<String, String> associations = new HashMap<>();

            for (var entry : node.getNode("associations").getChildrenMap().entrySet()) {
                String abbreviation = entry.getValue().getString().toLowerCase();
                String scheduleId = entry.getKey().toString().toLowerCase();
                associations.put(abbreviation, scheduleId);
            }

            return new TeacherScheduleInfo(url, associations);
        }

        @Override
        public void serialize(TypeToken<?> type, TeacherScheduleInfo obj, ConfigurationNode value) {

        }
    }
}
