package edu.zieit.scheduler.schedule.teacher;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.render.DocumentRenderer;
import edu.zieit.scheduler.api.schedule.PersonalRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.schedule.AbstractSchedule;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;
import java.util.stream.Collectors;

public class TeacherSchedule extends AbstractSchedule {

    private final String title;
    private final TeacherScheduleInfo info;
    private final Map<Person, List<TeacherDay>> days;

    public TeacherSchedule(TeacherScheduleInfo info, Sheet sheet, DocumentRenderer documentRenderer,
                           String title, Map<Person, List<TeacherDay>> days) throws IllegalArgumentException {
        super(sheet, documentRenderer);
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

    public List<TeacherDay> getDays(Person teacher) {
        return days.getOrDefault(teacher, Collections.emptyList());
    }

    public List<String> getTeachers() {
        return days.keySet().stream()
                .map(Person::toString)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    public PersonalRenderer getPersonalRenderer(Person person, Collection<Schedule> studentsSchedule) {
        return new TeacherScheduleRenderer(this, person, studentsSchedule);
    }

    @Override
    public String toString() {
        return "TeacherSchedule{" +
                "title='" + title + '\'' +
                ", days=" + days +
                '}';
    }
}
