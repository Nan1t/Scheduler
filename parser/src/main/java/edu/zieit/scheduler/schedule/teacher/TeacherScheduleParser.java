package edu.zieit.scheduler.schedule.teacher;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.api.render.DocumentRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleParseException;
import edu.zieit.scheduler.schedule.AbstractScheduleParser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;

public class TeacherScheduleParser extends AbstractScheduleParser {

    private static final SheetPoint POINT_DAY = new SheetPoint(1, 2);
    private static final SheetPoint POINT_TEACHER = new SheetPoint(0, 4);

    public TeacherScheduleParser(DocumentRenderer renderer) {
        super(renderer);
    }

    @Override
    public Collection<Schedule> parse(ScheduleInfo info) throws ScheduleParseException {
        Workbook workbook = loadWorkbook(info);
        Sheet sheet = workbook.getSheetAt(0);
        String title = getCell(sheet, 0, 0).getStringCellValue();

        Map<Person, List<TeacherDay>> teachers = new HashMap<>();
        int row = POINT_TEACHER.getRow();
        Cell teacherCell = getCell(sheet, row, POINT_TEACHER.getCol());

        while (teacherCell.getCellType() != CellType._NONE) {
            Person teacher = Person.from(teacherCell.getStringCellValue());
            List<TeacherDay> days = getDays(teacher, row);
            teachers.put(teacher, days);

            row++;
            teacherCell = getCell(sheet, row, POINT_TEACHER.getCol());
        }

        Schedule schedule = new TeacherSchedule((TeacherScheduleInfo) info, sheet, renderer, title, teachers);

        return Collections.singletonList(schedule);
    }

    private List<TeacherDay> getDays(Person teacher, final int row) {

        return null;
    }

}
