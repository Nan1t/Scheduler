package edu.zieit.scheduler.schedule.teacher;

import edu.zieit.scheduler.api.NamespaceKey;
import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.render.RenderException;
import edu.zieit.scheduler.api.schedule.ScheduleManager;
import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import edu.zieit.scheduler.schedule.students.ScheduleClass;
import edu.zieit.scheduler.schedule.students.ScheduleDay;
import edu.zieit.scheduler.schedule.students.StudentSchedule;
import edu.zieit.scheduler.util.ExcelUtil;
import napi.configurate.yaml.lang.Language;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.image.BufferedImage;
import java.util.*;

public class TeacherScheduleRenderer implements ScheduleRenderer {

    private final TeacherSchedule schedule;
    private final Person person;
    private final ScheduleManager manager;
    private final Language lang;

    private Font boldFont;

    public TeacherScheduleRenderer(TeacherSchedule schedule, Person person, ScheduleManager manager) {
        this.schedule = schedule;
        this.person = person;
        this.manager = manager;
        this.lang = manager.getLang();
    }

    @Override
    public BufferedImage render() throws RenderException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Collection<TeacherDay> days = schedule.getDays(person);

        initFonts(sheet);
        drawHeader(sheet);

        int lastRow = 3;
        for (TeacherDay day : days) {
            lastRow = drawDay(sheet, day, lastRow);
        }

        return schedule.getRenderer().render(sheet);
    }

    private void initFonts(Sheet sheet) {
        Workbook wb = sheet.getWorkbook();
        Font defFont = wb.getFontAt(sheet.getColumnStyle(0).getFontIndex());
        Font font = wb.createFont();
        font.setFontHeightInPoints(defFont.getFontHeightInPoints());
        font.setFontName(defFont.getFontName());
        font.setBold(true);

        this.boldFont = font;
    }

    private void drawHeader(Sheet sheet) {
        Cell titleCell = getOrCreateCell(sheet, 0, 0);
        Cell nameCell = getOrCreateCell(sheet, 1, 0);
        Cell dayTitleCell = getOrCreateCell(sheet, 2, 0);
        Cell classNumTitleCell = getOrCreateCell(sheet, 2, 1);
        Cell classTimeTitleCell = getOrCreateCell(sheet, 2, 2);
        Cell classesTitleCell = getOrCreateCell(sheet, 2, 3);

        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 1500);
        sheet.setColumnWidth(2, 1500);
        sheet.setColumnWidth(3, 12000);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));

        titleCell.setCellValue(schedule.getTitle());
        nameCell.setCellValue(person.toString());
        dayTitleCell.setCellValue(lang.of("schedule.render.head.days"));
        classNumTitleCell.setCellValue(lang.of("teachers.head.class.num"));
        classTimeTitleCell.setCellValue(lang.of("teachers.head.class.time"));
        classesTitleCell.setCellValue(lang.of("teachers.head.classes"));

        centerCell(titleCell);
        centerCell(nameCell);
        centerCell(dayTitleCell);
        centerCell(classNumTitleCell);
        centerCell(classTimeTitleCell);
        centerCell(classesTitleCell);

        borderCell(nameCell);
        borderCell(dayTitleCell);
        borderCell(classNumTitleCell);
        borderCell(classTimeTitleCell);
        borderCell(classesTitleCell);

        CellUtil.setFont(titleCell, boldFont);
        CellUtil.setFont(nameCell, boldFont);
        CellUtil.setFont(dayTitleCell, boldFont);
        CellUtil.setFont(classNumTitleCell, boldFont);
        CellUtil.setFont(classTimeTitleCell, boldFont);
        CellUtil.setFont(classesTitleCell, boldFont);
    }

    private int drawDay(Sheet sheet, TeacherDay teacherDay, int lastRow) {
        Cell dayCell = getOrCreateCell(sheet, lastRow, 0);

        dayCell.setCellValue(teacherDay.getName());

        int classRow = lastRow;

        for (var entry : teacherDay.getClasses().entrySet()) {
            int classNum = entry.getKey();
            TeacherClass teacherClass = entry.getValue();
            drawClass(sheet, dayCell, teacherDay, teacherClass, classNum, classRow);
            classRow += 4;
        }

        sheet.addMergedRegion(new CellRangeAddress(lastRow, classRow-1, 0, 0));

        borderCell(dayCell);
        CellUtil.setFont(dayCell, boldFont);

        return classRow;
    }

    private void drawClass(Sheet sheet, Cell dayCell, TeacherDay teacherDay, TeacherClass teacherClass, int classNum, int classRow) {
        Collection<StudentSchedule> studentSchedules = getScheduleByCourses(teacherClass);

        String time = null;

        Cell classNumCell = getOrCreateCell(sheet, classRow, 1);
        Cell classTimeCell = getOrCreateCell(sheet, classRow, 2);
        Cell titleCell = getOrCreateCell(sheet, classRow, 3);
        Cell typeCell = getOrCreateCell(sheet, classRow+1, 3);
        Cell groupsCell = getOrCreateCell(sheet, classRow+2, 3);
        Cell classroomCell = getOrCreateCell(sheet, classRow+3, 3);

        sheet.addMergedRegion(new CellRangeAddress(classRow, classRow+3, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(classRow, classRow+3, 2, 2));

        Set<String> names = new HashSet<>();
        Set<String> types = new HashSet<>();
        Set<String> groupsSet = new HashSet<>();
        Set<String> classrooms = new HashSet<>();

        for (StudentSchedule schedule : studentSchedules) {
            Optional<ScheduleDay> day = schedule.getDay(teacherDay.getName());

            if (day.isPresent()) {
                if (time == null)
                    time = day.get().getClassTime(classNum);

                Collection<ScheduleClass> classes = day.get().getClasses(classNum, person);

                for (ScheduleClass cl : classes) {
                    names.add(cl.getName());
                    types.add(cl.getType());
                    groupsSet.addAll(cl.getGroups());
                    classrooms.add(cl.getClassroom());
                }
            }
        }

        String title = String.join(",", names);
        String type = String.join(",", types);
        String groups = String.join(",", groupsSet);
        String classroom = String.join(",", classrooms);

        classNumCell.setCellValue(classNum);
        classTimeCell.setCellValue(time);

        if (names.isEmpty() && groupsSet.isEmpty()) {
            titleCell.setCellValue(String.format(lang.of("teachers.notfound"), teacherClass.raw()));
            sheet.addMergedRegion(new CellRangeAddress(classRow, classRow+3, 3, 3));
        } else {
            titleCell.setCellValue(title);
            typeCell.setCellValue(type);
            groupsCell.setCellValue(groups);
            classroomCell.setCellValue(classroom);

            centerCell(typeCell);
            centerCell(groupsCell);
            centerCell(classroomCell);
        }

        centerCell(titleCell);
        centerCell(dayCell);
        centerCell(classNumCell);
        centerCell(classTimeCell);

        borderCell(classNumCell);
        borderCell(classTimeCell);

        RegionUtil.setBorderRight(BorderStyle.THIN, ExcelUtil.getCellRange(titleCell), sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, ExcelUtil.getCellRange(typeCell), sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, ExcelUtil.getCellRange(groupsCell), sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, ExcelUtil.getCellRange(classroomCell), sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, ExcelUtil.getCellRange(classroomCell), sheet);
    }

    private Collection<StudentSchedule> getScheduleByCourses(TeacherClass teacherClass) {
        List<StudentSchedule> schedules = new LinkedList<>();

        for (String course : teacherClass.courses()) {
            NamespaceKey scheduleKey = this.schedule.getInfo().getAssociation(course);

            if (scheduleKey == null) // Try to find schedule by raw "pointer"
                scheduleKey = this.schedule.getInfo().getAssociation(teacherClass.raw());

            if (scheduleKey != null) {
                StudentSchedule sch = (StudentSchedule) manager.getStudentSchedule(scheduleKey);
                if (sch != null) schedules.add(sch);
            }
        }

        return schedules;
    }

    private void borderCell(Cell cell) {
        CellRangeAddress range = ExcelUtil.getCellRange(cell);

        RegionUtil.setBorderBottom(BorderStyle.THIN, range, cell.getSheet());
        RegionUtil.setBorderLeft(BorderStyle.THIN, range, cell.getSheet());
        RegionUtil.setBorderRight(BorderStyle.THIN, range, cell.getSheet());
        RegionUtil.setBorderTop(BorderStyle.THIN, range, cell.getSheet());
    }

    private void centerCell(Cell cell) {
        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
        CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);
    }

    private Cell getOrCreateCell(Sheet sheet, int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        if (row == null) row = sheet.createRow(rowNum);
        Cell cell = row.getCell(colNum);
        if (cell == null) cell = row.createCell(colNum);
        return cell;
    }

}
