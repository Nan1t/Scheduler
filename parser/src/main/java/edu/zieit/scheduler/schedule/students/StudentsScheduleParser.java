package edu.zieit.scheduler.schedule.students;

import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleParseException;
import edu.zieit.scheduler.schedule.AbstractScheduleParser;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class StudentsScheduleParser extends AbstractScheduleParser {

    @Override
    public Schedule parse(ScheduleInfo info) throws ScheduleParseException {
        Workbook workbook = loadWorkbook(info);
        Sheet sheet = workbook.getSheetAt(info.getSheetIndex());


        return null;
    }

}
