package edu.zieit.scheduler.schedule.consult;

import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.schedule.AbstractScheduleInfo;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

public class ConsultScheduleInfo extends AbstractScheduleInfo {

    private final SheetPoint dayPoint;
    private final SheetPoint teacherPoint;

    public ConsultScheduleInfo(URL url, SheetPoint dayPoint, SheetPoint teacherPoint) {
        super(url);
        this.dayPoint = dayPoint;
        this.teacherPoint = teacherPoint;
    }

    public SheetPoint dayPoint() {
        return dayPoint;
    }

    public SheetPoint teacherPoint() {
        return teacherPoint;
    }

    public static class Serializer implements TypeSerializer<ConsultScheduleInfo> {

        @Override
        public ConsultScheduleInfo deserialize(Type type, ConfigurationNode node) throws SerializationException {
            URL url;

            try {
                url = new URL(node.node("url").getString(""));
            } catch (MalformedURLException e) {
                throw new SerializationException("Incorrect or missing schedule URL");
            }

            SheetPoint dayPoint = node.node("day_point").get(SheetPoint.class);
            SheetPoint teacherPoint = node.node("teacher_point").get(SheetPoint.class);

            return new ConsultScheduleInfo(url, dayPoint, teacherPoint);
        }

        @Override
        public void serialize(Type type, @Nullable ConsultScheduleInfo info, ConfigurationNode node) throws SerializationException {
            if (info != null) {
                node.node("url").set(info.getUrl().toString());
                node.node("day_point").set(info.dayPoint());
                node.node("teacher_point").set(info.teacherPoint());
            }
        }
    }
}
