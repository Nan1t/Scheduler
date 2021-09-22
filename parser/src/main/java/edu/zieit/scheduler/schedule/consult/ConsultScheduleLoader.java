package edu.zieit.scheduler.schedule.consult;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.render.SheetRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleParseException;
import edu.zieit.scheduler.schedule.AbstractScheduleLoader;
import edu.zieit.scheduler.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ConsultScheduleLoader extends AbstractScheduleLoader {

    public ConsultScheduleLoader(SheetRenderer renderer) {
        super(renderer);
    }

    @Override
    public Collection<Schedule> load(ScheduleInfo inf) throws ScheduleParseException {
        if (!(inf instanceof ConsultScheduleInfo info))
            throw new ScheduleParseException("Incorrect schedule info class");

        Workbook workbook = loadWorkbook(info);
        Sheet sheet = workbook.getSheetAt(0);
        ConsultSchedule.Builder builder = ConsultSchedule.builder(sheet, renderer);
        String title = ExcelUtil.getCellValue(getCell(sheet, 2, 1)).strip();
        builder.title(title);
        builder.info(info);

        int row = info.teacherPoint().row();
        Cell teacherCell = getCell(sheet, row, info.teacherPoint().col());

        while (!ExcelUtil.isEmptyCell(teacherCell)) {
            Person person = Person.teacher(ExcelUtil.getCellValue(teacherCell));
            List<ConsultDay> days = parseDays(info, sheet, row);

            if (!days.isEmpty()) {
                builder.withWeek(person, days);
            }

            teacherCell = getCell(sheet, ++row, info.teacherPoint().col());
        }

        return Collections.singletonList(builder.build());
    }

    private List<ConsultDay> parseDays(ConsultScheduleInfo info, Sheet sheet, int row) {
        List<ConsultDay> days = new LinkedList<>();
        int col = info.dayPoint().col();
        Cell dayCell = getCell(sheet, info.dayPoint().row(), col);

        while (!ExcelUtil.isEmptyCell(dayCell)) {
            Cell timeCell = getCell(sheet, row, col);
            String dayName = ExcelUtil.getCellValue(dayCell).strip();
            String time = ExcelUtil.getCellValue(timeCell).strip();

            if (!time.isEmpty()) {
                days.add(new ConsultDay(dayName, time));
            }

            dayCell = getCell(sheet, info.dayPoint().row(), ++col);
        }

        return days;
    }

}
