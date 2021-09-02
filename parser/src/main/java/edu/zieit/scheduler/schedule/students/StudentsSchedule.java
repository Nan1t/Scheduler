package edu.zieit.scheduler.schedule.students;

import edu.zieit.scheduler.api.render.DocumentRenderer;
import edu.zieit.scheduler.api.schedule.PersonalRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.schedule.AbstractSchedule;
import edu.zieit.scheduler.schedule.EmptyPersonalRenderer;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Represents student's schedule
 */
public class StudentsSchedule extends AbstractSchedule {

    public StudentsSchedule(ScheduleInfo info, Sheet sheet, DocumentRenderer documentRenderer)
            throws IllegalArgumentException {
        super(info, sheet, documentRenderer);
    }

    @Override
    public PersonalRenderer getPersonalRenderer() {
        return new EmptyPersonalRenderer();
    }
}
