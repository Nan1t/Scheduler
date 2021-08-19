package edu.zieit.scheduler.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.awt.*;

public final class SizeUtil {

    private SizeUtil() { }

    /**
     * Get real cell width in pixels
     * @param cell Required cell to calculate width
     * @return Cell width in pixels
     */
    public static int getCellWidth(Cell cell) {
        int width = cell.getSheet().getColumnWidth(cell.getColumnIndex());
        CellRangeAddress range = ExcelUtil.getCellRange(cell);

        if (range != null) {
            width = 0;
            for (int i = range.getFirstColumn(); i <= range.getLastColumn(); i++) {
                width += cell.getSheet().getColumnWidth(i);
            }
        }

        return colWidthToPixel(width);
    }

    public static int getCellHeight(Cell cell) {
        int height = cell.getRow().getHeight();
        CellRangeAddress range = ExcelUtil.getCellRange(cell);

        if (range != null) {
            Sheet sheet = cell.getSheet();
            height = 0;
            for (int i = range.getFirstRow(); i <= range.getLastRow(); i++) {
                Row row = sheet.getRow(i);
                if (row != null)
                    height += row.getHeight();
            }
        }

        return twipsToPixels(height);
    }

    /**
     * Convert columns width to pixels
     * @param width Columns width
     * @return Pixels value
     */
    public static int colWidthToPixel(int width) {
        return (int) Math.round((double) width / 28.4390244);
    }

    /**
     * Convert points (e.g font size points) to pixels
     * @param points Points value
     * @return Pixels value
     */
    public static int pointsToPixels(double points) {
        //return (int) Math.round(points * 1.3333333333333333);
        return (int) Math.round(points * 0.75);
    }

    /**
     * Convert twips to pixels
     * @param twips Twips value
     * @return Pixels value
     */
    public static int twipsToPixels(int twips) {
        return (int) Math.round((double) twips * 0.0666666667);
    }

    public static Dimension getSheetSize(Sheet sheet) {
        int maxWidth = 0;
        int height = 0;
        int lastRow = ExcelUtil.getLastRow(sheet);

        for (Row row : sheet) {
            if (row.getRowNum() <= lastRow)
                height += twipsToPixels(row.getHeight());

            for (Cell cell : row) {
                int width = SizeUtil.getCellWidth(cell);
                if (width > maxWidth)
                    maxWidth = width;
            }
        }

        return new Dimension(maxWidth, height);
    }

}
