package edu.zieit.scheduler.schedule.consult;

import com.google.common.reflect.TypeToken;
import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.schedule.AbstractScheduleInfo;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

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
        public ConsultScheduleInfo deserialize(TypeToken<?> type, ConfigurationNode node) throws ObjectMappingException {
            URL url;

            try {
                url = new URL(node.getNode("url").getString(""));
            } catch (MalformedURLException e) {
                throw new ObjectMappingException("Incorrect or missing schedule URL");
            }

            SheetPoint dayPoint = node.getNode("day_point").getValue(TypeToken.of(SheetPoint.class));
            SheetPoint teacherPoint = node.getNode("teacher_point").getValue(TypeToken.of(SheetPoint.class));

            return new ConsultScheduleInfo(url, dayPoint, teacherPoint);
        }

        @Override
        public void serialize(TypeToken<?> type, ConsultScheduleInfo obj, ConfigurationNode value) throws ObjectMappingException {

        }
    }
}
