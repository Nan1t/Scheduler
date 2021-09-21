package edu.zieit.scheduler.schedule.students;

import com.google.common.reflect.TypeToken;
import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.schedule.AbstractScheduleInfo;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.net.MalformedURLException;
import java.net.URL;

public class StudentScheduleInfo extends AbstractScheduleInfo {

    private final String displayName;
    private final SheetPoint dayPoint;
    private final SheetPoint groupPoint;

    public StudentScheduleInfo(URL url, String displayName, SheetPoint dayPoint, SheetPoint groupPoint) {
        super(url);
        this.displayName = displayName;
        this.dayPoint = dayPoint;
        this.groupPoint = groupPoint;
    }

    public String getDisplayName() {
        return displayName;
    }

    public SheetPoint getDayPoint() {
        return dayPoint;
    }

    public SheetPoint getGroupPoint() {
        return groupPoint;
    }

    public static class Serializer implements TypeSerializer<StudentScheduleInfo> {

        @Override
        public StudentScheduleInfo deserialize(TypeToken<?> type, ConfigurationNode node)
                throws ObjectMappingException {
            URL url;

            try {
                url = new URL(node.getNode("url").getString(""));
            } catch (MalformedURLException e) {
                throw new ObjectMappingException("Incorrect or missing schedule URL");
            }

            String displayName = node.getNode("name").getString();
            SheetPoint dayPoint = node.getNode("day_point").getValue(TypeToken.of(SheetPoint.class));
            SheetPoint groupPoint = node.getNode("group_point").getValue(TypeToken.of(SheetPoint.class));

            return new StudentScheduleInfo(url, displayName, dayPoint, groupPoint);
        }

        @Override
        public void serialize(TypeToken<?> type, StudentScheduleInfo obj, ConfigurationNode value) {

        }

    }

}
