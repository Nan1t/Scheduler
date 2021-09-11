package edu.zieit.scheduler.schedule.teacher;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.api.render.DocumentRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleParseException;
import edu.zieit.scheduler.schedule.AbstractScheduleParser;
import edu.zieit.scheduler.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;
import java.util.stream.Collectors;

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
        int row = POINT_TEACHER.row();
        Cell teacherCell = getCell(sheet, row, POINT_TEACHER.col());

        while (!ExcelUtil.isEmptyCell(teacherCell)) {
            Person teacher = Person.from(teacherCell.getStringCellValue());
            List<TeacherDay> days = getDays(sheet, row);
            teachers.put(teacher, days);

            row++;
            teacherCell = getCell(sheet, row, POINT_TEACHER.col());
        }

        Schedule schedule = new TeacherSchedule((TeacherScheduleInfo) info, sheet, renderer, title, teachers);

        return Collections.singletonList(schedule);
    }

    private List<TeacherDay> getDays(Sheet sheet, final int teacherRow) {
        var days = new LinkedList<TeacherDay>();
        Cell dayCell = getCell(sheet, POINT_DAY.row(), POINT_DAY.col());
        int classNumRow = POINT_DAY.row() + 1;

        while (!ExcelUtil.isEmptyCell(dayCell)) {
            var range = ExcelUtil.getCellRange(dayCell);

            if (range == null) continue;

            var dayBuilder = TeacherDay.builder();

            dayBuilder.displayName(dayCell.getStringCellValue());

            for (int col = range.getFirstColumn(); col < range.getLastColumn(); col++) {
                Cell classNumCell = getCell(sheet, classNumRow, col);
                Cell coursesCell = getCell(sheet, teacherRow, col);
                int classNum = Integer.parseInt(ExcelUtil.getCellValue(classNumCell));
                String coursesRaw = ExcelUtil.getCellValue(coursesCell);
                Collection<String> courses = Arrays.stream(coursesRaw.split(","))
                        .map(s->s.trim().toLowerCase())
                        .collect(Collectors.toList());

                dayBuilder.addCourses(classNum, courses);
            }

            days.add(dayBuilder.build());
            dayCell = getCell(sheet, POINT_DAY.row(), range.getLastColumn() + 1);
        }

        return days;
    }

}
