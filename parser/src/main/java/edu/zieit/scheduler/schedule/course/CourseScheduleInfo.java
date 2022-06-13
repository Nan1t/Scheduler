package edu.zieit.scheduler.schedule.course;

import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.schedule.AbstractScheduleInfo;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

public class CourseScheduleInfo extends AbstractScheduleInfo {

    private final String displayName;
    private final SheetPoint dayPoint;
    private final SheetPoint groupPoint;

    public CourseScheduleInfo(URL url, String displayName, SheetPoint dayPoint, SheetPoint groupPoint) {
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

    public static class Serializer implements TypeSerializer<CourseScheduleInfo> {

        @Override
        public CourseScheduleInfo deserialize(Type type, ConfigurationNode node) throws SerializationException {
            URL url;

            try {
                url = new URL(node.node("url").getString(""));
            } catch (MalformedURLException e) {
                throw new SerializationException("Incorrect or missing schedule URL");
            }

            String displayName = node.node("name").getString();
            SheetPoint dayPoint = node.node("day_point").get(SheetPoint.class);
            SheetPoint groupPoint = node.node("group_point").get(SheetPoint.class);

            return new CourseScheduleInfo(url, displayName, dayPoint, groupPoint);
        }

        @Override
        public void serialize(Type type, @Nullable CourseScheduleInfo info, ConfigurationNode node) throws SerializationException {
            if (info != null) {
                node.node("url").set(info.getUrl().toString());
                node.node("name").set(info.getDisplayName());
                node.node("day_point").set(info.getDayPoint());
                node.node("group_point").set(info.getGroupPoint());
            }
        }
    }

}
