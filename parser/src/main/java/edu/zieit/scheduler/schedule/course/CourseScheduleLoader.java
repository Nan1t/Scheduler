package edu.zieit.scheduler.schedule.course;

import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.Regexs;
import edu.zieit.scheduler.api.render.SheetRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleParseException;
import edu.zieit.scheduler.schedule.AbstractScheduleLoader;
import edu.zieit.scheduler.schedule.TimeTable;
import edu.zieit.scheduler.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;
import java.util.regex.Matcher;

public class CourseScheduleLoader extends AbstractScheduleLoader {

    public CourseScheduleLoader(SheetRenderer renderer) {
        super(renderer);
    }

    @Override
    public Collection<Schedule> load(ScheduleInfo info) throws ScheduleParseException {
        if (!(info instanceof CourseScheduleInfo sinfo))
            return Collections.emptyList();

        Workbook workbook = loadWorkbook(info);

        if (workbook.getNumberOfSheets() == 1) {
            Sheet sheet = workbook.getSheetAt(0);
            Schedule schedule = parseSchedule(sinfo, sheet);

            return Collections.singletonList(schedule);
        } else {
            List<Schedule> schedules = new LinkedList<>();

            for (Sheet sheet : workbook) {
                CourseSchedule schedule = parseSchedule(sinfo, sheet);
                schedule.setDisplayName(sinfo.getDisplayName() + " " + sheet.getSheetName());
                schedules.add(schedule);
            }

            for (Schedule schedule : schedules) {
                ((CourseSchedule)schedule).setFileGroup(schedules);
            }

            return schedules;
        }
    }

    private CourseSchedule parseSchedule(CourseScheduleInfo info, Sheet sheet) throws ScheduleParseException {
        Map<Integer, CourseDay> days = new HashMap<>();
        int row = info.getDayPoint().row();
        Cell dayCell = getCell(sheet, row, info.getDayPoint().col());

        while (!ExcelUtil.isEmptyCell(dayCell)) {
            CellRangeAddress range = ExcelUtil.getCellRange(dayCell);
            CourseDay day = parseDay(info, sheet, dayCell, range);
            int dayIndex = TimeTable.getDayIndex(day.getName());

            days.put(dayIndex, day);
            row = range.getLastRow() + 1;
            dayCell = getCell(sheet, row, info.getDayPoint().col());
        }

        NamespacedKey key;

        if (sheet.getWorkbook().getNumberOfSheets() > 1) {
            key = NamespacedKey.of(info.getId(), sheet.getSheetName().toLowerCase());
        } else {
            key = NamespacedKey.of(info.getId());
        }

        return new CourseSchedule(info, key, sheet, renderer, days);
    }

    private CourseDay parseDay(CourseScheduleInfo info, Sheet sheet, Cell dayCell, CellRangeAddress range) {
        CourseDay day = new CourseDay();
        int classNumCol = range.getLastColumn() + 1;
        int classTimeCol = classNumCol + 1;

        int row = range.getFirstRow();

        while (row < range.getLastRow()) {
            Cell classNumCell = getCell(sheet, row, classNumCol);
            Cell classTimeCell = getCell(sheet, row, classTimeCol);
            CellRangeAddress classNumRange = ExcelUtil.getCellRange(classNumCell);

            int classIndex = Integer.parseInt(ExcelUtil.getCellValue(classNumCell));
            String classTime = ExcelUtil.getCellValue(classTimeCell);

            TimeTable.addClassTime(classIndex, classTime);

            Collection<CourseClass> classes = parseClasses(info, sheet, row, classIndex);

            for (CourseClass courseClass : classes) {
                day.addClass(classIndex, courseClass);
            }

            row = classNumRange.getLastRow() + 1;
        }

        List<String> list = ExcelUtil.getCellValue(dayCell)
                .lines()
                .toList();

        try {
            day.setName(list.get(0).strip());
        } catch (IndexOutOfBoundsException e) {
            // Ignore
        }

        try {
            day.setDate(list.get(1).strip());
        } catch (IndexOutOfBoundsException e) {
            // Ignore
        }

        return day;
    }

    private Collection<CourseClass> parseClasses(CourseScheduleInfo info, Sheet sheet, int classRow, int classIndex) {
        List<CourseClass> classes = new LinkedList<>();
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

            if (Regexs.TEACHER_INLINE.matcher(teacherName).find()) {
                // Teachers and classrooms in one line
                teacherName += "," + classrooms;
                String[] parts = teacherName.split(",");

                for (String part : parts) {
                    Matcher matcher = Regexs.TEACHER_INLINE.matcher(part);

                    if (matcher.find()) {
                        String teacher = matcher.group(1);
                        String classroom = matcher.group(2);
                        CourseClass courseClass = new CourseClass(classIndex, name);

                        courseClass.setType(type);
                        courseClass.setTeacher(Person.teacher(teacher));
                        courseClass.setClassroom(classroom);

                        groups.forEach(courseClass::addGroup);

                        classes.add(courseClass);
                    }
                }
            } else {
                // Only one teacher
                CourseClass courseClass = new CourseClass(classIndex, name);
                Person teacher = Person.empty();

                if (Regexs.TEACHER.matcher(teacherName).find()) {
                    teacher = Person.teacher(teacherName);
                }

                courseClass.setType(type);
                courseClass.setTeacher(teacher);
                courseClass.setClassroom(classrooms);

                groups.forEach(courseClass::addGroup);

                classes.add(courseClass);
            }

            col = range.getLastColumn() + 1;
            classCell = getCell(sheet, classRow, col);
        }

        return classes;
    }

    private Collection<String> parseGroups(CourseScheduleInfo info, Sheet sheet, CellRangeAddress range) {
        List<String> groups = new LinkedList<>();
        for (int col = range.getFirstColumn(); col <= range.getLastColumn(); col++) {
            Cell cell = getCell(sheet, info.getGroupPoint().row(), col);
            String group = ExcelUtil.getCellValue(cell);
            groups.add(clearGroup(group));
        }
        return groups;
    }

    private String clearGroup(String group) {
        return group.replace(" ", "").toUpperCase();
    }
}
