package edu.zieit.scheduler.schedule.students;

import edu.zieit.scheduler.api.render.DocumentRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleParseException;
import edu.zieit.scheduler.schedule.AbstractScheduleParser;
import edu.zieit.scheduler.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class StudentScheduleParser extends AbstractScheduleParser {

    public StudentScheduleParser(DocumentRenderer renderer) {
        super(renderer);
    }

    @Override
    public Collection<Schedule> parse(ScheduleInfo info) throws ScheduleParseException {
        if (!(info instanceof StudentScheduleInfo sinfo))
            return Collections.emptyList();

        Workbook workbook = loadWorkbook(info);

        if (workbook.getNumberOfSheets() == 1) {
            Sheet sheet = workbook.getSheetAt(0);
            Schedule schedule = parseSchedule(sinfo, sheet);
            return Collections.singletonList(schedule);
        } else {
            List<Schedule> schedules = new LinkedList<>();

            for (Sheet sheet : workbook) {
                StudentSchedule schedule = parseSchedule(sinfo, sheet);
                schedule.setDisplayName(sinfo.getDisplayName() + " " + sheet.getSheetName());
                schedules.add(schedule);
            }

            return schedules;
        }
    }

    private StudentSchedule parseSchedule(StudentScheduleInfo info, Sheet sheet) throws ScheduleParseException {


        return null;
    }

    private ScheduleDay parseDay(StudentScheduleInfo info, Sheet sheet, Cell dayCell, CellRangeAddress range) {
        var builder = ScheduleDay.builder();
        int classNumCol = range.getLastColumn() + 1;
        int classTimeCol = classNumCol + 1;

        int row = range.getFirstRow();

        while (row < range.getLastRow()) {
            Cell classCell = getCell(sheet, row, classNumCol);
            CellRangeAddress classRange = ExcelUtil.getCellRange(classCell);
            int classRows = classRange.getLastRow() - classRange.getFirstRow();



            row += classRows;
        }

        builder.name(ExcelUtil.getCellValue(dayCell));

        return builder.build();
    }


}
