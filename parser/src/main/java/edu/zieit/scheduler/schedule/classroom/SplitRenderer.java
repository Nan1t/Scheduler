package edu.zieit.scheduler.schedule.classroom;

import edu.zieit.scheduler.api.render.RenderException;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.schedule.AbstractScheduleRenderer;
import edu.zieit.scheduler.schedule.TimeTable;
import edu.zieit.scheduler.schedule.course.CourseClass;
import edu.zieit.scheduler.schedule.course.CourseDay;
import edu.zieit.scheduler.util.ExcelUtil;
import edu.zieit.scheduler.api.config.Language;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class SplitRenderer extends AbstractScheduleRenderer {

    private final Collection<String> classrooms;
    private final ScheduleService manager;
    private final Language lang;

    public SplitRenderer(Collection<String> classrooms, ScheduleService manager) {
        this.classrooms = classrooms;
        this.manager = manager;
        this.lang = manager.getLang();
    }

    @Override
    public BufferedImage render() throws RenderException {
        Sheet sheet = renderBase();
        return manager.renderer().render(sheet);
    }

    @Override
    public byte[] renderBytes() throws RenderException {
        Sheet sheet = renderBase();
        return manager.renderer().renderBytes(sheet);
    }

    private Sheet renderBase() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        initFonts(sheet);
        drawHeader(sheet);

        int col = 3;
        for (String cl : classrooms) {
            Optional<Schedule> opt = manager.getClassroomSchedule(cl);

            if (opt.isPresent() && opt.get() instanceof ClassroomSchedule schedule) {
                int row = 2;

                for (CourseDay day : schedule.getDays().values()) {
                    row = drawDay(sheet, day, row, col);
                }
            }

            col++;
        }

        return sheet;
    }

    private void drawHeader(Sheet sheet) {
        Cell nameCell = getOrCreateCell(sheet, 0, 0);
        Cell dayTitleCell = getOrCreateCell(sheet, 1, 0);
        Cell classNumTitleCell = getOrCreateCell(sheet, 1, 1);
        Cell classTimeTitleCell = getOrCreateCell(sheet, 1, 2);

        int col = 3;
        for (String cl : classrooms) {
            Cell classroomCell = getOrCreateCell(sheet, 1, col);

            classroomCell.setCellValue(cl);
            centerCell(classroomCell);
            borderCell(classroomCell);
            CellUtil.setFont(classroomCell, boldFont);

            sheet.setColumnWidth(col, 12000);

            col++;
        }

        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 1500);
        sheet.setColumnWidth(2, 1500);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2 + classrooms.size()));

        nameCell.setCellValue("Comp auds");
        dayTitleCell.setCellValue(lang.of("schedule.render.head.days"));
        classNumTitleCell.setCellValue(lang.of("schedule.render.head.classnum"));
        classTimeTitleCell.setCellValue(lang.of("schedule.render.head.classtime"));

        centerCell(nameCell);
        centerCell(dayTitleCell);
        centerCell(classNumTitleCell);
        centerCell(classTimeTitleCell);

        borderCell(nameCell);
        borderCell(dayTitleCell);
        borderCell(classNumTitleCell);
        borderCell(classTimeTitleCell);

        CellUtil.setFont(nameCell, boldFont);
        CellUtil.setFont(dayTitleCell, boldFont);
        CellUtil.setFont(classNumTitleCell, boldFont);
        CellUtil.setFont(classTimeTitleCell, boldFont);
    }

    private int drawDay(Sheet sheet, CourseDay day, int row, int col) {
        Cell dayCell = getOrCreateCell(sheet, row, 0);

        setWrapText(dayCell, true);
        dayCell.setCellValue(day.getName() + "\n" + day.getDate());

        int classRow = row;

        for (var entry : day.getClasses().entrySet()) {
            drawClass(sheet, entry.getValue(), entry.getKey(), classRow, col);
            classRow += 2;
        }

        if (classRow > row) {
            sheet.addMergedRegionUnsafe(new CellRangeAddress(row, classRow-1, 0, 0));
        }

        centerCell(dayCell);
        borderCell(dayCell);
        CellUtil.setFont(dayCell, boldFont);

        return classRow;
    }

    private void drawClass(Sheet sheet, Collection<CourseClass> classes, int classNum, int row, int col) {
        String names = classes.stream()
                .map(CourseClass::getName)
                .distinct()
                .collect(Collectors.joining(", "));

        String teachers = classes.stream()
                .map(c -> c.getTeacher().toString())
                .distinct()
                .collect(Collectors.joining(", "));

        Cell classNumCell = getOrCreateCell(sheet, row, 1);
        Cell classTimeCell = getOrCreateCell(sheet, row, 2);

        Cell titleCell = getOrCreateCell(sheet, row, col);
        Cell teacherCell = getOrCreateCell(sheet, row+1, col);

        sheet.addMergedRegionUnsafe(new CellRangeAddress(row, row+1, 1, 1));
        sheet.addMergedRegionUnsafe(new CellRangeAddress(row, row+1, 2, 2));

        classNumCell.setCellValue(classNum);
        classTimeCell.setCellValue(TimeTable.getTime(classNum));

        titleCell.setCellValue(names);
        teacherCell.setCellValue(teachers);

        centerCell(classNumCell);
        centerCell(classTimeCell);

        centerCell(titleCell);
        centerCell(teacherCell);

        borderCell(classNumCell);
        borderCell(classTimeCell);

        RegionUtil.setBorderRight(BorderStyle.THIN, ExcelUtil.getCellRange(titleCell), sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, ExcelUtil.getCellRange(teacherCell), sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, ExcelUtil.getCellRange(teacherCell), sheet);
    }
}
