package edu.zieit.scheduler.schedule.students;

import com.google.common.reflect.TypeToken;
import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.schedule.AbstractScheduleInfo;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.net.MalformedURLException;
import java.net.URL;

public class StudentsScheduleInfo extends AbstractScheduleInfo {

    private final String displayName;
    private final SheetPoint dayPoint;

    private StudentsScheduleInfo(URL url, String displayName, SheetPoint dayPoint) {
        super(url);
        this.displayName = displayName;
        this.dayPoint = dayPoint;
    }

    public String getDisplayName() {
        return displayName;
    }

    public SheetPoint getDayPoint() {
        return dayPoint;
    }

    public static class Serializer implements TypeSerializer<StudentsScheduleInfo> {

        @Override
        public StudentsScheduleInfo deserialize(TypeToken<?> type, ConfigurationNode node)
                throws ObjectMappingException {
            URL url;

            try {
                url = new URL(node.getNode("url").getString(""));
            } catch (MalformedURLException e) {
                throw new ObjectMappingException("Incorrect or missing schedule URL");
            }

            String displayName = node.getNode("name").getString();
            SheetPoint dayPoint = node.getNode("day_point").getValue(TypeToken.of(SheetPoint.class));

            return new StudentsScheduleInfo(url, displayName, dayPoint);
        }

        @Override
        public void serialize(TypeToken<?> type, StudentsScheduleInfo obj, ConfigurationNode value) {

        }

    }

}
