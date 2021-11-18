package edu.zieit.scheduler.schedule.course;

import edu.zieit.scheduler.api.render.RenderException;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.schedule.AbstractScheduleRenderer;
import edu.zieit.scheduler.schedule.TimeTable;
import edu.zieit.scheduler.util.ExcelUtil;
import napi.configurate.yaml.lang.Language;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Optional;

public class GroupScheduleRenderer extends AbstractScheduleRenderer {

    private final String group;
    private final CourseSchedule schedule;
    private final ScheduleService manager;
    private final Language lang;

    public GroupScheduleRenderer(String group, CourseSchedule schedule, ScheduleService manager) {
        this.group = group;
        this.schedule = schedule;
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

        Collection<CourseDay> days = schedule.getGroupDays(group);

        int row = 2;
        for (CourseDay day : days) {
            row = drawDay(sheet, day, row);
        }

        return sheet;
    }

    private void drawHeader(Sheet sheet) {
        Cell nameCell = getOrCreateCell(sheet, 0, 0);
        Cell dayTitleCell = getOrCreateCell(sheet, 1, 0);
        Cell classNumTitleCell = getOrCreateCell(sheet, 1, 1);
        Cell classTimeTitleCell = getOrCreateCell(sheet, 1, 2);
        Cell classesTitleCell = getOrCreateCell(sheet, 1, 3);

        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 1500);
        sheet.setColumnWidth(2, 1500);
        sheet.setColumnWidth(3, 12000);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

        nameCell.setCellValue(group.toUpperCase());
        dayTitleCell.setCellValue(lang.of("schedule.render.head.days"));
        classNumTitleCell.setCellValue(lang.of("schedule.render.head.classnum"));
        classTimeTitleCell.setCellValue(lang.of("schedule.render.head.classtime"));
        classesTitleCell.setCellValue(lang.of("schedule.render.head.classes"));

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

        CellUtil.setFont(nameCell, boldFont);
        CellUtil.setFont(dayTitleCell, boldFont);
        CellUtil.setFont(classNumTitleCell, boldFont);
        CellUtil.setFont(classTimeTitleCell, boldFont);
        CellUtil.setFont(classesTitleCell, boldFont);
    }

    private int drawDay(Sheet sheet, CourseDay day, int row) {
        Cell dayCell = getOrCreateCell(sheet, row, 0);

        setWrapText(dayCell, true);
        dayCell.setCellValue(day.getName() + "\n" + day.getDate());

        int classRow = row;

        for (var entry : day.getClasses().entrySet()) {
            int classNum = entry.getKey();
            Optional<CourseClass> cl = day.getGroupClass(classNum, group);

            if (cl.isPresent()) {
                drawClass(sheet, cl.get(), classNum, classRow);
                classRow += 4;
            }
        }

        if (classRow > row) {
            sheet.addMergedRegion(new CellRangeAddress(row, classRow-1, 0, 0));
        }

        centerCell(dayCell);
        borderCell(dayCell);
        CellUtil.setFont(dayCell, boldFont);

        return classRow;
    }

    private void drawClass(Sheet sheet, CourseClass cl, int classNum, int row) {
        Cell classNumCell = getOrCreateCell(sheet, row, 1);
        Cell classTimeCell = getOrCreateCell(sheet, row, 2);

        Cell titleCell = getOrCreateCell(sheet, row, 3);
        Cell typeCell = getOrCreateCell(sheet, row+1, 3);
        Cell teacherCell = getOrCreateCell(sheet, row+2, 3);
        Cell classroomCell = getOrCreateCell(sheet, row+3, 3);

        sheet.addMergedRegion(new CellRangeAddress(row, row+3, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(row, row+3, 2, 2));

        classNumCell.setCellValue(classNum);
        classTimeCell.setCellValue(TimeTable.getTime(classNum));

        titleCell.setCellValue(cl.getName());
        typeCell.setCellValue(cl.getType());
        teacherCell.setCellValue(cl.getTeacher().toString());
        classroomCell.setCellValue(cl.getClassroom());

        centerCell(classNumCell);
        centerCell(classTimeCell);

        centerCell(titleCell);
        centerCell(typeCell);
        centerCell(teacherCell);
        centerCell(classroomCell);

        borderCell(classNumCell);
        borderCell(classTimeCell);

        RegionUtil.setBorderRight(BorderStyle.THIN, ExcelUtil.getCellRange(titleCell), sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, ExcelUtil.getCellRange(typeCell), sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, ExcelUtil.getCellRange(teacherCell), sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, ExcelUtil.getCellRange(classroomCell), sheet);
        RegionUtil.setBorderBottom(BorderStyle.THIN, ExcelUtil.getCellRange(classroomCell), sheet);
    }
}
