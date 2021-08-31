package edu.zieit.scheduler.schedule.students;

import com.google.common.reflect.TypeToken;
import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.net.URL;

public class StudentsScheduleInfo extends ScheduleInfo {

    private final String id;
    private final String displayName;
    private final SheetPoint dayPoint;
    private final SheetPoint classNumPoint;
    private final SheetPoint groupsPoint;

    private StudentsScheduleInfo(URL url, int sheetIndex, String id, String displayName,
                                SheetPoint dayPoint, SheetPoint classNumPoint, SheetPoint groupsPoint) {
        super(url, sheetIndex);
        this.id = id;
        this.displayName = displayName;
        this.dayPoint = dayPoint;
        this.classNumPoint = classNumPoint;
        this.groupsPoint = groupsPoint;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public SheetPoint getDayPoint() {
        return dayPoint;
    }

    public SheetPoint getClassNumPoint() {
        return classNumPoint;
    }

    public SheetPoint getGroupsPoint() {
        return groupsPoint;
    }

    public static class Serializer implements TypeSerializer<StudentsScheduleInfo> {

        @Override
        public StudentsScheduleInfo deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
            return null;
        }

        @Override
        public void serialize(TypeToken<?> type, StudentsScheduleInfo obj, ConfigurationNode value) {

        }

    }

}
