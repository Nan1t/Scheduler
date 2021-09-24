package edu.zieit.scheduler.schedule.teacher;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.render.SheetRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleManager;
import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import edu.zieit.scheduler.schedule.AbstractSchedule;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;
import java.util.stream.Collectors;

public class TeacherSchedule extends AbstractSchedule {

    private final String title;
    private final TeacherScheduleInfo info;
    private final Map<Person, List<TeacherDay>> days;

    public TeacherSchedule(TeacherScheduleInfo info, Sheet sheet, SheetRenderer sheetRenderer,
                           String title, Map<Person, List<TeacherDay>> days) {
        super(info, sheet, sheetRenderer);
        this.info = info;
        this.title = title;
        this.days = days;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public TeacherScheduleInfo getInfo() {
        return info;
    }

    public Collection<TeacherDay> getDays(Person person) {
        return days.getOrDefault(person, Collections.emptyList());
    }

    public Collection<String> getTeachers() {
        return days.keySet().stream()
                .map(Person::toString)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    @Override
    public ScheduleRenderer getPersonalRenderer(Person person, ScheduleManager manager) {
        return new TeacherScheduleRenderer(this, person, manager);
    }

    @Override
    public String toString() {
        return "TeacherSchedule{" +
                "title='" + title + '\'' +
                ", days=" + days +
                '}';
    }
}
