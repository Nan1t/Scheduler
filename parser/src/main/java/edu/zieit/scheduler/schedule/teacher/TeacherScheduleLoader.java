package edu.zieit.scheduler.schedule.teacher;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.api.render.SheetRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleParseException;
import edu.zieit.scheduler.schedule.AbstractScheduleLoader;
import edu.zieit.scheduler.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;
import java.util.stream.Collectors;

public class TeacherScheduleLoader extends AbstractScheduleLoader {

    private static final SheetPoint POINT_DAY = new SheetPoint(1, 2);
    private static final SheetPoint POINT_TEACHER = new SheetPoint(0, 4);

    public TeacherScheduleLoader(SheetRenderer renderer) {
        super(renderer);
    }

    @Override
    public Collection<Schedule> load(ScheduleInfo info) throws ScheduleParseException {
        Workbook workbook = loadWorkbook(info);
        Sheet sheet = workbook.getSheetAt(0);
        String title = ExcelUtil.getCellValue(getCell(sheet, 0, 0));

        Map<Person, List<TeacherDay>> teachers = new HashMap<>();
        int row = POINT_TEACHER.row();
        Cell teacherCell = getCell(sheet, row, POINT_TEACHER.col());

        while (!ExcelUtil.isEmptyCell(teacherCell)) {
            Person person = Person.teacher(ExcelUtil.getCellValue(teacherCell));
            List<TeacherDay> days = getDays(sheet, row);
            teachers.put(person, days);

            row++;
            teacherCell = getCell(sheet, row, POINT_TEACHER.col());
        }

        Schedule schedule = new TeacherSchedule((TeacherScheduleInfo) info, sheet, renderer, title, teachers);

        return Collections.singletonList(schedule);
    }

    private List<TeacherDay> getDays(Sheet sheet, final int teacherRow) {
        List<TeacherDay> days = new LinkedList<>();
        Cell dayCell = getCell(sheet, POINT_DAY.row(), POINT_DAY.col());
        int classNumRow = POINT_DAY.row() + 1;

        while (!ExcelUtil.isEmptyCell(dayCell)) {
            var range = ExcelUtil.getCellRange(dayCell);

            if (range == null) continue;

            String dayName = ExcelUtil.getCellValue(dayCell);
            TeacherDay day = new TeacherDay(dayName);

            for (int col = range.getFirstColumn(); col < range.getLastColumn(); col++) {
                Cell classNumCell = getCell(sheet, classNumRow, col);
                Cell coursesCell = getCell(sheet, teacherRow, col);
                int classNum = Integer.parseInt(ExcelUtil.getCellValue(classNumCell));
                String coursesRaw = ExcelUtil.getCellValue(coursesCell);

                if (!coursesRaw.isBlank()) {
                    Collection<String> courses = Arrays.stream(coursesRaw.split(","))
                            .map(s->s.trim().toLowerCase())
                            .collect(Collectors.toList());

                    day.addClass(classNum, new TeacherClass(classNum, coursesRaw, courses));
                }
            }

            if (!day.isEmpty())
                days.add(day);

            dayCell = getCell(sheet, POINT_DAY.row(), range.getLastColumn() + 1);
        }

        return days;
    }

}
