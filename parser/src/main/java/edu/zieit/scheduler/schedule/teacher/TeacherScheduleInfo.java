package edu.zieit.scheduler.schedule.teacher;

import edu.zieit.scheduler.schedule.AbstractScheduleInfo;

import java.net.URL;
import java.util.Map;

public class TeacherScheduleInfo extends AbstractScheduleInfo {

    private final Map<String, String> associations;

    public TeacherScheduleInfo(URL url, String id, Map<String, String> associations) {
        super(url, id);
        this.associations = associations;
    }

    public String getScheduleId(String abbreviation) {
        return associations.get(abbreviation);
    }
}