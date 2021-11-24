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
    private final List<CourseDay> days;
    private final Map<String, Collection<CourseDay>> daysByGroup;
    private final Map<String, Collection<CourseDay>> daysByClassroom;

    private String displayName;
    private Collection<Schedule> fileGroup;

    public CourseSchedule(CourseScheduleInfo info, NamespacedKey key, Sheet sheet, SheetRenderer sheetRenderer,
                          List<CourseDay> days) {
        super(key, sheet, sheetRenderer);
        this.info = info;
        this.days = days;
        this.daysByGroup = new HashMap<>();
        this.daysByClassroom = new HashMap<>();

        remapByGroup();
        remapByClassroom();

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

    public Collection<Schedule> getFileGroup() {
        return fileGroup != null ? fileGroup : Collections.emptyList();
    }

    public void setFileGroup(Collection<Schedule> group) {
        this.fileGroup = group;
    }

    public boolean hasGroup() {
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

    public Collection<String> getClassrooms() {
        return daysByClassroom.keySet();
    }

    public Collection<CourseDay> getClassroomDays(String classroom) {
        return daysByClassroom.getOrDefault(classroom, Collections.emptyList());
    }

    public void addClassroomDay(String classroom, CourseDay day) {
        daysByClassroom.computeIfAbsent(classroom, c -> new LinkedHashSet<>()).add(day);
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
        for (CourseDay day : days) {
            for (CourseClass cl : day.getAllClasses()) {
                for (String group : cl.getGroups()) {
                    addGroupDay(group, day);
                }
            }
        }
    }

    private void remapByClassroom() {
        for (CourseDay day : days) {
            for (CourseClass cl : day.getAllClasses()) {
                String classroom = cl.getClassroom().replace(" ", "");

                if (!classroom.isEmpty())
                    addClassroomDay(classroom, day);
            }
        }
    }
}
