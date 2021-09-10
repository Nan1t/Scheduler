package edu.zieit.scheduler.schedule.teacher;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.render.DocumentRenderer;
import edu.zieit.scheduler.api.schedule.PersonalRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.schedule.AbstractSchedule;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TeacherSchedule extends AbstractSchedule {

    private final TeacherScheduleInfo info;
    private final Map<Person, List<TeacherDay>> days;

    public TeacherSchedule(TeacherScheduleInfo info, Sheet sheet, DocumentRenderer documentRenderer,
                           Map<Person, List<TeacherDay>> days) throws IllegalArgumentException {
        super(sheet, documentRenderer);
        this.info = info;
        this.days = days;
    }

    @Override
    public TeacherScheduleInfo getInfo() {
        return info;
    }

    public List<TeacherDay> getDays(Person teacher) {
        return days.getOrDefault(teacher, Collections.emptyList());
    }

    public PersonalRenderer getPersonalRenderer(Person person, Collection<Schedule> studentsSchedule) {
        return new TeacherScheduleRenderer(this, person, studentsSchedule);
    }
}
