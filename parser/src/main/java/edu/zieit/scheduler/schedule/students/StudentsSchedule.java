package edu.zieit.scheduler.schedule.students;

import edu.zieit.scheduler.Person;
import edu.zieit.scheduler.api.render.DocumentRenderer;
import edu.zieit.scheduler.api.schedule.PersonalRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.schedule.AbstractSchedule;
import edu.zieit.scheduler.schedule.EmptyPersonalRenderer;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Collection;
import java.util.List;

/**
 * Represents student's schedule
 */
public class StudentsSchedule extends AbstractSchedule {

    private final List<ScheduleDay> days;

    public StudentsSchedule(ScheduleInfo info, Sheet sheet, DocumentRenderer documentRenderer,
                            List<ScheduleDay> days) throws IllegalArgumentException {
        super(info, sheet, documentRenderer);
        this.days = days;
    }

    public Collection<ScheduleDay> getDays() {
        return days;
    }

    @Override
    public PersonalRenderer getPersonalRenderer() {
        return new EmptyPersonalRenderer();
    }
}
