package edu.zieit.scheduler.schedule.students;

import edu.zieit.scheduler.api.render.DocumentRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.schedule.AbstractSchedule;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;

/**
 * Represents student's schedule
 */
public class StudentSchedule extends AbstractSchedule {

    private final StudentScheduleInfo info;
    private final List<ScheduleDay> days;
    private String displayName;
    private Set<Schedule> group;

    public StudentSchedule(StudentScheduleInfo info, Sheet sheet, DocumentRenderer documentRenderer,
                           List<ScheduleDay> days) throws Exception {
        super(sheet, documentRenderer);
        this.info = info;
        this.days = days;
        setDisplayName(info.getDisplayName());
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Collection<ScheduleDay> getDays() {
        return days;
    }

    public Collection<Schedule> getGroup() {
        return group != null ? group : Collections.emptyList();
    }

    public void addToGroup(Schedule schedule) {
        if (group == null) group = new HashSet<>();
        group.add(schedule);
    }

    public boolean hasGroup() {
        return group == null || group.isEmpty();
    }

    @Override
    public StudentScheduleInfo getInfo() {
        return info;
    }
}
