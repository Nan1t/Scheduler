package edu.zieit.scheduler.util;

import org.apache.poi.ss.usermodel.Cell;

import java.awt.*;

public final class StyleUtil {

    private StyleUtil() { }

    public static Color toAwtColor(org.apache.poi.ss.usermodel.Color color) {
        return new Color(0xFFFFFF);
    }

    public static Font toAwtFont(org.apache.poi.ss.usermodel.Font src) {
        int style = src.getBold() ? Font.BOLD
                : src.getItalic() ? Font.ITALIC
                : Font.PLAIN;
        return new Font(src.getFontName(), style, src.getFontHeightInPoints());
    }

    public static Font getCellFont(Cell cell) {
        int index = cell.getCellStyle().getFontIndex();
        return toAwtFont(cell.getSheet().getWorkbook().getFontAt(index));
    }
}
