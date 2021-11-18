package edu.zieit.scheduler.schedule;

import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import edu.zieit.scheduler.util.ExcelUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;

public abstract class AbstractScheduleRenderer implements ScheduleRenderer {

    protected Font boldFont;

    protected void initFonts(Sheet sheet) {
        Workbook wb = sheet.getWorkbook();
        Font defFont = wb.getFontAt(sheet.getColumnStyle(0).getFontIndex());
        Font font = wb.createFont();
        font.setFontHeightInPoints(defFont.getFontHeightInPoints());
        font.setFontName(defFont.getFontName());
        font.setBold(true);

        this.boldFont = font;
    }

    protected void setWrapText(Cell cell, boolean val) {
        CellStyle style = cell.getCellStyle();
        style.setWrapText(val);
        cell.setCellStyle(style);
    }

    protected void borderCell(Cell cell) {
        CellRangeAddress range = ExcelUtil.getCellRange(cell);

        RegionUtil.setBorderBottom(BorderStyle.THIN, range, cell.getSheet());
        RegionUtil.setBorderLeft(BorderStyle.THIN, range, cell.getSheet());
        RegionUtil.setBorderRight(BorderStyle.THIN, range, cell.getSheet());
        RegionUtil.setBorderTop(BorderStyle.THIN, range, cell.getSheet());
    }

    protected void centerCell(Cell cell) {
        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
        CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);
    }

    protected Cell getOrCreateCell(Sheet sheet, int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        if (row == null) row = sheet.createRow(rowNum);
        Cell cell = row.getCell(colNum);
        if (cell == null) cell = row.createCell(colNum);
        return cell;
    }

}
