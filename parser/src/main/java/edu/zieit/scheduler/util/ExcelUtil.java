package edu.zieit.scheduler.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

public final class ExcelUtil {

    private ExcelUtil() { }

    /**
     * Get range of cell if it merged
     * @param cell Required cell
     * @return Defined range of cell or null if cell is not merged
     */
    public static CellRangeAddress getCellRange(Cell cell) {
        if (cell != null) {
            for (CellRangeAddress region : cell.getSheet().getMergedRegions()) {
                if (region.isInRange(cell)) return region;
            }
            return getSingleRange(cell);
        }
        return null;
    }

    /**
     * Get single cell range (first row == last row and first col == last col)
     * @param cell non-null cell
     * @return Single range
     */
    public static CellRangeAddress getSingleRange(Cell cell) {
        return new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(), cell.getColumnIndex());
    }

    /**
     * Get last row with some content.
     * @param sheet Sheet of document
     * @return Index of row
     */
    public static int getLastRow(Sheet sheet) {
        for (int i = sheet.getLastRowNum(); i > 0; i--) {
            Row row = sheet.getRow(i);

            if (row != null) {
                for (Cell cell : row) {
                    CellType type = cell.getCellType();

                    if (type != CellType._NONE && type != CellType.BLANK)
                        return row.getRowNum();
                }
            }
        }

        return 0;
    }

    public static String getCellValue(Cell cell) {
        if (cell == null) return "";
        try {
            return cell.getStringCellValue();
        } catch (Exception e) {
            return String.valueOf((int) cell.getNumericCellValue());
        }
    }

    public static boolean isEmptyCell(Cell cell) {
        return cell == null || cell.getCellType() == CellType._NONE || cell.getCellType() == CellType.BLANK;
    }

}
