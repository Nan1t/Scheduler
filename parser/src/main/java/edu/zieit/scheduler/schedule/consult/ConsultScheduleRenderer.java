package edu.zieit.scheduler.schedule.consult;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.render.RenderException;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import edu.zieit.scheduler.util.ExcelUtil;
import napi.configurate.yaml.lang.Language;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.image.BufferedImage;
import java.util.Collection;

public class ConsultScheduleRenderer implements ScheduleRenderer {

    private final ConsultSchedule schedule;
    private final Person teacher;
    private final Language lang;

    private Font boldFont;

    public ConsultScheduleRenderer(ConsultSchedule schedule, Person teacher, ScheduleService manager) {
        this.schedule = schedule;
        this.teacher = teacher;
        this.lang = manager.getLang();
    }

    @Override
    public BufferedImage render() throws RenderException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        initFonts(sheet);
        drawHeader(sheet);

        Collection<ConsultDay> week = schedule.getWeek(teacher);

        int dayRow = 2;

        for (ConsultDay day : week) {
            Cell dayCell = getOrCreateCell(sheet, dayRow, 0);
            Cell timeCell = getOrCreateCell(sheet, dayRow, 1);

            dayCell.setCellValue(day.dayName());
            timeCell.setCellValue(day.time());

            CellUtil.setFont(dayCell, boldFont);

            centerCell(dayCell);
            centerCell(timeCell);

            borderCell(dayCell);
            borderCell(timeCell);

            dayRow++;
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
        Cell teacherNameCell = getOrCreateCell(sheet, 0, 0);
        Cell dayTitleCell = getOrCreateCell(sheet, 1, 0);
        Cell timeTitleCell = getOrCreateCell(sheet, 1, 1);

        sheet.setColumnWidth(0, 3500);
        sheet.setColumnWidth(1, 4000);

        teacherNameCell.setCellValue(teacher.toString());
        dayTitleCell.setCellValue(lang.of("schedule.render.head.days"));
        timeTitleCell.setCellValue(lang.of("consult.head.time"));

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

        centerCell(teacherNameCell);
        centerCell(dayTitleCell);
        centerCell(timeTitleCell);

        borderCell(dayTitleCell);
        borderCell(timeTitleCell);

        CellUtil.setFont(teacherNameCell, boldFont);
        CellUtil.setFont(dayTitleCell, boldFont);
        CellUtil.setFont(timeTitleCell, boldFont);
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
