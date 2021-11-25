package edu.zieit.scheduler.schedule.course;

import com.google.common.base.Preconditions;
import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.api.render.SheetRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.schedule.AbstractSchedule;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents student's schedule
 */
public class CourseSchedule extends AbstractSchedule {

    private final CourseScheduleInfo info;
    private final Map<Integer, CourseDay> days;
    private final Map<String, Collection<CourseDay>> daysByGroup;

    private String displayName;
    private Collection<CourseSchedule> fileGroup;

    public CourseSchedule(CourseScheduleInfo info, NamespacedKey key, Sheet sheet, SheetRenderer sheetRenderer,
                          Map<Integer, CourseDay> days) {
        super(key, sheet, sheetRenderer);
        this.info = info;
        this.days = days;
        this.daysByGroup = new HashMap<>();

        remapByGroup();
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
        return days.values();
    }

    public Optional<CourseDay> getDay(int index) {
        return Optional.ofNullable(days.get(index));
    }

    public Collection<CourseSchedule> getFileGroup() {
        return fileGroup != null ? fileGroup : Collections.emptyList();
    }

    public void setFileGroup(Collection<Schedule> group) {
        if (this.fileGroup == null)
            fileGroup = new LinkedList<>();

        for (Schedule member : group) {
            fileGroup.add((CourseSchedule) member);
        }
    }

    public boolean hasFileGroup() {
        return fileGroup != null && !fileGroup.isEmpty();
    }

    public Collection<String> getGroupNames() {
        return daysByGroup.keySet();
    }

    public Collection<CourseDay> getGroupDays(String group) {
        return daysByGroup.getOrDefault(group, Collections.emptyList());
    }

    public void addGroupDay(String group, CourseDay day) {
        daysByGroup.computeIfAbsent(group, g -> new LinkedHashSet<>()).add(day);
    }

    @Override
    public CourseScheduleInfo getInfo() {
        return info;
    }

    @Override
    public String toString() {
        Collection<String> groups = fileGroup != null ? fileGroup.stream()
                .map(el -> ((CourseSchedule)el).getDisplayName())
                .collect(Collectors.toList()) : Collections.emptyList();

        return "StudentSchedule{" +
                "days=" + days +
                ", displayName='" + displayName + '\'' +
                ", group=" + groups +
                '}';
    }

    @Override
    public ScheduleRenderer getPersonalRenderer(Object data, ScheduleService manager) {
        return new GroupScheduleRenderer(data.toString(), this, manager);
    }

    private void remapByGroup() {
        for (CourseDay day : getDays()) {
            for (CourseClass cl : day.getAllClasses()) {
                for (String group : cl.getGroups()) {
                    addGroupDay(group, day);
                }
            }
        }
    }
}
