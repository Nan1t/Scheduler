package edu.zieit.scheduler.schedule.students;

import edu.zieit.scheduler.api.render.DocumentRenderer;
import edu.zieit.scheduler.api.schedule.PersonalRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.schedule.AbstractSchedule;
import edu.zieit.scheduler.schedule.EmptyPersonalRenderer;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Represents student's schedule
 */
public class StudentsSchedule extends AbstractSchedule {

    public StudentsSchedule(ScheduleInfo info, Workbook workbook, DocumentRenderer documentRenderer)
            throws IllegalArgumentException {
        super(info, workbook, documentRenderer);
    }

    @Override
    public PersonalRenderer getPersonalRenderer() {
        return new EmptyPersonalRenderer(this);
    }
}
