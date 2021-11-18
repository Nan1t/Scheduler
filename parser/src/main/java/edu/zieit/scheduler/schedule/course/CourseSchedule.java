package edu.zieit.scheduler.schedule.course;

import com.google.common.base.Preconditions;
import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.api.render.SheetRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.schedule.AbstractSchedule;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents student's schedule
 */
public class CourseSchedule extends AbstractSchedule {

    private final CourseScheduleInfo info;
    private final List<CourseDay> days;
    private String displayName;
    private Collection<Schedule> group;

    public CourseSchedule(CourseScheduleInfo info, NamespacedKey key, Sheet sheet, SheetRenderer sheetRenderer,
                          List<CourseDay> days) {
        super(key, sheet, sheetRenderer);
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

    public Collection<CourseDay> getDays() {
        return days;
    }

    public Optional<CourseDay> getDay(String dayName) {
        return days.stream()
                .filter(day -> day.getName().equalsIgnoreCase(dayName))
                .findFirst();
    }

    public Collection<Schedule> getGroup() {
        return group != null ? group : Collections.emptyList();
    }

    public void setGroup(Collection<Schedule> group) {
        this.group = group;
    }

    public boolean hasGroup() {
        return group != null && !group.isEmpty();
    }

    @Override
    public CourseScheduleInfo getInfo() {
        return info;
    }

    @Override
    public String toString() {
        Collection<String> groups = group != null ? group.stream()
                .map(el -> ((CourseSchedule)el).getDisplayName())
                .collect(Collectors.toList()) : Collections.emptyList();

        return "StudentSchedule{" +
                "days=" + days +
                ", displayName='" + displayName + '\'' +
                ", group=" + groups +
                '}';
    }
}
