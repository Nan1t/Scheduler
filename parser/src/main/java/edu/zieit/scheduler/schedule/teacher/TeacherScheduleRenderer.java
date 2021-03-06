package edu.zieit.scheduler.schedule.teacher;

import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.render.RenderException;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.schedule.AbstractScheduleRenderer;
import edu.zieit.scheduler.schedule.TimeTable;
import edu.zieit.scheduler.schedule.course.CourseClass;
import edu.zieit.scheduler.schedule.course.CourseDay;
import edu.zieit.scheduler.schedule.course.CourseSchedule;
import edu.zieit.scheduler.util.ExcelUtil;
import edu.zieit.scheduler.api.config.Language;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.Collectors;

public class TeacherScheduleRenderer extends AbstractScheduleRenderer {

    private final TeacherSchedule schedule;
    private final Person person;
    private final ScheduleService manager;
    private final Language lang;

    private boolean useVariants = false;

    public TeacherScheduleRenderer(TeacherSchedule schedule, Person person, ScheduleService manager) {
        this.schedule = schedule;
        this.person = person;
        this.manager = manager;
        this.lang = manager.getLang();
    }

    @Override
    public BufferedImage render() throws RenderException {
        Sheet sheet = renderBase();
        return schedule.getRenderer().render(sheet);
    }

    @Override
    public byte[] renderBytes() throws RenderException {
        Sheet sheet = renderBase();
        return schedule.getRenderer().renderBytes(sheet);
    }

    private Sheet renderBase() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Collection<TeacherDay> days = schedule.getDays(person);

        initFonts(sheet);
        drawHeader(sheet);

        int lastRow = 3;
        for (TeacherDay day : days) {
            lastRow = drawDay(sheet, day, lastRow);
        }

        if (useVariants) {
            Cell cell = getOrCreateCell(sheet, lastRow + 1, 0);
            cell.setCellValue(lang.of("schedule.render.teacher.var.info"));
        }

        return sheet;
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
        classNumTitleCell.setCellValue(lang.of("schedule.render.head.classnum"));
        classTimeTitleCell.setCellValue(lang.of("schedule.render.head.classtime"));
        classesTitleCell.setCellValue(lang.of("schedule.render.head.classes"));

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
            drawClass(sheet, teacherDay, teacherClass, classNum, classRow);
            classRow += 4;
        }

        if (classRow > lastRow) {
            sheet.addMergedRegion(new CellRangeAddress(lastRow, classRow-1, 0, 0));
        }

        centerCell(dayCell);
        borderCell(dayCell);
        CellUtil.setFont(dayCell, boldFont);

        return classRow;
    }

    private void drawClass(Sheet sheet, TeacherDay teacherDay, TeacherClass teacherClass, int classNum, int classRow) {
        Collection<CourseSchedule> courseSchedules = getScheduleByCourses(teacherClass);

        Cell classNumCell = getOrCreateCell(sheet, classRow, 1);
        Cell classTimeCell = getOrCreateCell(sheet, classRow, 2);
        Cell titleCell = getOrCreateCell(sheet, classRow, 3);
        Cell typeCell = getOrCreateCell(sheet, classRow+1, 3);
        Cell groupsCell = getOrCreateCell(sheet, classRow+2, 3);
        Cell classroomCell = getOrCreateCell(sheet, classRow+3, 3);

        setWrapText(groupsCell, true);

        sheet.addMergedRegion(new CellRangeAddress(classRow, classRow+3, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(classRow, classRow+3, 2, 2));

        Set<String> names = new HashSet<>();
        Set<String> types = new HashSet<>();
        Set<String> groupsSet = new HashSet<>();
        Set<String> classrooms = new HashSet<>();

        for (CourseSchedule schedule : courseSchedules) {
            Collection<CourseClass> classes = findClasses(schedule, teacherDay, classNum, person, true);

            for (CourseClass cl : classes) {
                names.add(cl.getName());
                types.add(cl.getType());
                groupsSet.addAll(cl.getGroups());
                classrooms.add(cl.getClassroom());
            }
        }

        String title = String.join(",", names);
        String type = String.join(",", types);
        String groups = String.join(",", groupsSet);
        String classroom = String.join(",", classrooms);

        classNumCell.setCellValue(classNum);
        classTimeCell.setCellValue(TimeTable.getTime(classNum));

        if (names.isEmpty() && groupsSet.isEmpty()) {
            String value = getClassVariants(teacherDay.getIndex(), teacherClass.index(), courseSchedules);

            if (value == null) {
                value = String.format(lang.of("schedule.render.teacher.notfound"), teacherClass.raw());
            } else {
                useVariants = true;
                String variants = lang.of("schedule.render.teacher.variants");
                value = variants + " " + value;
                CellUtil.setFont(titleCell, littleFont);
            }

            titleCell.setCellValue(value);
            sheet.addMergedRegion(new CellRangeAddress(classRow, classRow+3, 3, 3));
        } else {
            titleCell.setCellValue(title);
            typeCell.setCellValue(type);
            groupsCell.setCellValue(groups);
            classroomCell.setCellValue(classroom);

            centerCell(typeCell);
            centerCell(groupsCell);
            //centerCell(classroomCell);
        }

        centerCell(titleCell);
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

    private String getClassVariants(int tday, int tclass, Collection<CourseSchedule> courseSchedules) {
        List<CourseClass> variants = new LinkedList<>();

        for (CourseSchedule schedule : courseSchedules) {
            schedule.getDay(tday).ifPresent(courseDay ->
                    variants.addAll(courseDay.getClasses(tclass)));
        }

        return variants.size() > 0 ? variants.stream()
                .distinct()
                .map(cl -> cl.getName() + " ("+ cl.getClassroom() +")")
                .collect(Collectors.joining(lang.of("schedule.render.teacher.or"))) : null;
    }

    private Collection<CourseClass> findClasses(
            CourseSchedule schedule,
            TeacherDay day,
            int classNum,
            Person teacher,
            boolean retAlternate
    ) {
        int dayIndex = TimeTable.getDayIndex(day.getName());
        Optional<CourseDay> dayOpt = schedule.getDay(dayIndex);

        if (dayOpt.isPresent()) {
            Collection<CourseClass> classes = dayOpt.get().getClasses(classNum, person);

            if (!classes.isEmpty())
                return classes;
        }

        return retAlternate
                ? getAlternateClasses(schedule, day, classNum, teacher)
                : Collections.emptyList();
    }

    private Collection<CourseClass> getAlternateClasses(
            CourseSchedule schedule,
            TeacherDay day,
            int classNum,
            Person teacher
    ) {
        for (CourseSchedule member : schedule.getFileGroup()) {
            Collection<CourseClass> classes = findClasses(member, day, classNum, teacher, false);

            if (!classes.isEmpty())
                return classes;
        }

        return Collections.emptyList();
    }

    private Collection<CourseSchedule> getScheduleByCourses(TeacherClass teacherClass) {
        List<CourseSchedule> schedules = new LinkedList<>();

        for (String course : teacherClass.courses()) {
            NamespacedKey scheduleKey = this.schedule.getInfo().getAssociation(course);

            if (scheduleKey == null) // Try to find schedule by raw "pointer"
                scheduleKey = this.schedule.getInfo().getAssociation(teacherClass.raw());

            if (scheduleKey != null) {
                CourseSchedule sch = (CourseSchedule) manager.getCourseSchedule(scheduleKey);
                if (sch != null) schedules.add(sch);
            }
        }

        return schedules;
    }

}
