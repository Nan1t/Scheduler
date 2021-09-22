package edu.zieit.scheduler.render;

import edu.zieit.scheduler.util.ExcelUtil;
import edu.zieit.scheduler.api.render.SheetRenderer;
import edu.zieit.scheduler.util.SizeUtil;
import edu.zieit.scheduler.util.StyleUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

/**
 * Renderer for excel documents. WIP
 */
public class AwtRenderer extends SheetRenderer {

    private static final int LINE_SPACING = 8;

    private int x, y;
    private int width, height;

    @Override
    public BufferedImage render(Sheet sheet) {
        Dimension size = SizeUtil.getSheetSize(sheet);
        BufferedImage image = new BufferedImage(size.width + 250, size.height + 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.setColor(Color.BLACK);

        Set<String> visited = new HashSet<>();

        for (int r = sheet.getFirstRowNum(); r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);

            if (row == null) continue;

            height = SizeUtil.twipsToPixels(row.getHeight());

            for (int c = row.getFirstCellNum(); c <= row.getLastCellNum(); c++) {
                Cell cell = row.getCell(c);

                width = SizeUtil.colWidthToPixel(sheet.getColumnWidth(c));

                if (cell != null) {
                    CellRangeAddress range = ExcelUtil.getCellRange(cell);

                    width = SizeUtil.getCellWidth(cell);

                    if (range != null) {
                        if (visited.contains(range.formatAsString())) {
                            x += width;
                            continue;
                        }

                        visited.add(range.formatAsString());
                    }

                    height = SizeUtil.getCellHeight(cell);

                    drawText(graphics, cell);
                    drawBorder(graphics, cell);
                }

                x += width;
            }

            x = 0;
            y += height;
        }

        return image;
    }

    private void drawText(Graphics2D graphics, Cell cell) {
        String[] lines = ExcelUtil.getCellValue(cell).split("\\n");
        Font font = StyleUtil.getCellFont(cell);
        int fontSize = SizeUtil.pointsToPixels(font.getSize());
        int posY = calcVerticalPos(cell, lines.length, fontSize);

        graphics.setFont(font);

        for (String line :lines) {
            int lineWidth = line.length() * fontSize;
            int posX = calcHorizontalPos(cell, lineWidth);

            graphics.drawString(line, posX, posY);
            posY += SizeUtil.pointsToPixels(font.getSize()) + LINE_SPACING;
        }
    }

    private void drawBorder(Graphics2D graphics, Cell cell) {
        BorderStyle top = cell.getCellStyle().getBorderTop();
        BorderStyle right = cell.getCellStyle().getBorderRight();
        BorderStyle bottom = cell.getCellStyle().getBorderBottom();
        BorderStyle left = cell.getCellStyle().getBorderLeft();

        int topThickness = 0;
        int rightThickness = 0;
        int bottomThickness = 0;
        int leftThickness = 0;

        switch (top) {
            case THIN:
                topThickness = 1;
                break;
            case MEDIUM:
                topThickness = 2;
                break;
            case THICK:
                topThickness = 3;
                break;
            default:
                break;
        }

        switch (right) {
            case THIN:
                rightThickness = 1;
                break;
            case MEDIUM:
                rightThickness = 2;
                break;
            case THICK:
                rightThickness = 3;
                break;
            default:
                break;
        }

        switch (bottom) {
            case THIN:
                bottomThickness = 1;
                break;
            case MEDIUM:
                bottomThickness = 2;
                break;
            case THICK:
                bottomThickness = 3;
                break;
            default:
                break;
        }

        switch (left) {
            case THIN:
                leftThickness = 1;
                break;
            case MEDIUM:
                leftThickness = 2;
                break;
            case THICK:
                leftThickness = 3;
                break;
            default:
                break;
        }

        graphics.fillRect(x, y, width, topThickness);
        graphics.fillRect(x + width, y, rightThickness, height);
        graphics.fillRect(x, y + height, width, bottomThickness);
        graphics.fillRect(x, y, leftThickness, height);
    }

    private int calcHorizontalPos(Cell cell, int lineWidth) {
        int pos = x;

        switch (cell.getCellStyle().getAlignment()) {
            default:
                break;
            case CENTER_SELECTION:
            case CENTER:
                pos = x + width / 2 - lineWidth / 2;
                break;
            case RIGHT:
                pos = x + width - lineWidth;
                break;
        }

        return pos;
    }

    private int calcVerticalPos(Cell cell, int lines, int fontSize) {
        int linesHeight = lines * fontSize + LINE_SPACING * (lines - 1);
        int pos = y + linesHeight;

        switch (cell.getCellStyle().getVerticalAlignment()) {
            case CENTER:
                pos = y + height / 2 + linesHeight / 2;
                break;
            case BOTTOM:
                pos = y + height - linesHeight / 2;
                break;
            default:
                break;
        }

        return pos;
    }

}
