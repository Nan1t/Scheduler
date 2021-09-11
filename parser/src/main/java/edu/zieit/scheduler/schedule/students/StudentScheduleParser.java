package edu.zieit.scheduler.schedule.students;

import edu.zieit.scheduler.api.render.DocumentRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleParseException;
import edu.zieit.scheduler.schedule.AbstractScheduleParser;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Collection;

public class StudentScheduleParser extends AbstractScheduleParser {

    public StudentScheduleParser(DocumentRenderer renderer) {
        super(renderer);
    }

    @Override
    public Collection<Schedule> parse(ScheduleInfo info) throws ScheduleParseException {
        Workbook workbook = loadWorkbook(info);



        return null;
    }

}
