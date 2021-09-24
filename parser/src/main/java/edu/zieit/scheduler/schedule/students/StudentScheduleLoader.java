package edu.zieit.scheduler.schedule.students;

import edu.zieit.scheduler.api.NamespaceKey;
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
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

public class StudentScheduleLoader extends AbstractScheduleLoader {

    public StudentScheduleLoader(SheetRenderer renderer) {
        super(renderer);
    }

    @Override
    public Collection<Schedule> load(ScheduleInfo info) throws ScheduleParseException {
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

            for (Schedule schedule : schedules) {
                ((StudentSchedule)schedule).setGroup(schedules);
            }

            return schedules;
        }
    }

    private StudentSchedule parseSchedule(StudentScheduleInfo info, Sheet sheet) throws ScheduleParseException {
        List<ScheduleDay> days = new LinkedList<>();
        int row = info.getDayPoint().row();
        Cell dayCell = getCell(sheet, row, info.getDayPoint().col());

        while (!ExcelUtil.isEmptyCell(dayCell)) {
            CellRangeAddress range = ExcelUtil.getCellRange(dayCell);

            days.add(parseDay(info, sheet, dayCell, range));

            row = range.getLastRow() + 1;
            dayCell = getCell(sheet, row, info.getDayPoint().col());
        }

        NamespaceKey key;

        if (sheet.getWorkbook().getNumberOfSheets() > 1) {
            key = NamespaceKey.of(info.getId(), sheet.getSheetName().toLowerCase());
        } else {
            key = NamespaceKey.of(info.getId());
        }

        return new StudentSchedule(info, key, sheet, renderer, days);
    }

    private ScheduleDay parseDay(StudentScheduleInfo info, Sheet sheet, Cell dayCell, CellRangeAddress range) {
        var builder = ScheduleDay.builder();
        int classNumCol = range.getLastColumn() + 1;
        int classTimeCol = classNumCol + 1;

        int row = range.getFirstRow();

        while (row < range.getLastRow()) {
            Cell classNumCell = getCell(sheet, row, classNumCol);
            Cell classTimeCell = getCell(sheet, row, classTimeCol);
            CellRangeAddress classNumRange = ExcelUtil.getCellRange(classNumCell);

            int classIndex = Integer.parseInt(ExcelUtil.getCellValue(classNumCell));
            String classTime = ExcelUtil.getCellValue(classTimeCell);

            builder.addTimePoint(classIndex, classTime);

            Collection<ScheduleClass> classes = parseClasses(info, sheet, row);

            for (ScheduleClass scheduleClass : classes) {
                builder.addClass(classIndex, scheduleClass);
            }

            row = classNumRange.getLastRow() + 1;
        }

        builder.name(ExcelUtil.getCellValue(dayCell)
                .lines()
                .findFirst()
                .orElse("day")
                .strip());

        return builder.build();
    }

    private Collection<ScheduleClass> parseClasses(StudentScheduleInfo info, Sheet sheet, int classRow) {
        List<ScheduleClass> classes = new LinkedList<>();
        int col = info.getGroupPoint().col();
        Cell classCell = getCell(sheet, classRow, col);

        while (classCell != null && col < classCell.getRow().getLastCellNum()) {
            Cell typeCell = getCell(sheet, classRow + 1, col);
            Cell teacherCell = getCell(sheet, classRow + 2, col);
            Cell classroomCell = getCell(sheet, classRow + 3, col);

            if (ExcelUtil.isEmptyCell(classCell)
                    && ExcelUtil.isEmptyCell(teacherCell)
                    && ExcelUtil.isEmptyCell(classroomCell)) {
                col++;
                classCell = getCell(sheet, classRow, col);
                continue;
            }

            CellRangeAddress range = ExcelUtil.getCellRange(classCell);
            String name = ExcelUtil.getCellValue(classCell).strip();
            String type = ExcelUtil.getCellValue(typeCell).strip();
            String teacherName = ExcelUtil.getCellValue(teacherCell).strip();
            String classrooms = ExcelUtil.getCellValue(classroomCell).strip();
            Collection<String> groups = parseGroups(info, sheet, range);

            if (Person.REGEX_TEACHER_INLINE.matcher(teacherName).find()) {
                // Teachers and classrooms in one line
                teacherName += "," + classrooms;
                String[] parts = teacherName.split(",");

                for (String part : parts) {
                    Matcher matcher = Person.REGEX_TEACHER_INLINE.matcher(part);

                    if (matcher.find()) {
                        String teacher = matcher.group(1);
                        String classroom = matcher.group(2);
                        ScheduleClass.Builder builder = ScheduleClass.builder();

                        builder.name(name)
                                .type(type)
                                .teacher(teacher)
                                .classroom(classroom);

                        groups.forEach(builder::withGroup);

                        classes.add(builder.build());
                    }
                }
            } else {
                // Only one teacher
                ScheduleClass.Builder builder = ScheduleClass.builder();
                Person teacher = Person.empty();

                if (Person.REGEX_TEACHER.matcher(teacherName).find()) {
                    teacher = Person.teacher(teacherName);
                }

                builder.name(name)
                        .type(type)
                        .teacher(teacher)
                        .classroom(classrooms);

                groups.forEach(builder::withGroup);
                classes.add(builder.build());
            }

            col = range.getLastColumn() + 1;
            classCell = getCell(sheet, classRow, col);
        }

        return classes;
    }

    private Collection<String> parseGroups(StudentScheduleInfo info, Sheet sheet, CellRangeAddress range) {
        List<String> groups = new LinkedList<>();
        for (int col = range.getFirstColumn(); col <= range.getLastColumn(); col++) {
            Cell cell = getCell(sheet, info.getGroupPoint().row(), col);
            String group = ExcelUtil.getCellValue(cell);
            groups.add(group);
        }
        return groups;
    }

}
