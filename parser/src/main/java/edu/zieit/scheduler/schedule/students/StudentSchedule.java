package edu.zieit.scheduler.schedule.students;

import com.google.common.base.Preconditions;
import edu.zieit.scheduler.api.render.SheetRenderer;
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

    public StudentSchedule(StudentScheduleInfo info, Sheet sheet, SheetRenderer sheetRenderer,
                           List<ScheduleDay> days) {
        super(sheet, sheetRenderer);
        this.info = info;
        this.days = days;
        setDisplayName(info.getDisplayName());
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        Preconditions.checkNotNull(displayName, "Display name cannot be null");
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
