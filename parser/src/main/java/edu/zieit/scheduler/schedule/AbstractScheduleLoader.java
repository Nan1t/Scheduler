package edu.zieit.scheduler.schedule;

import edu.zieit.scheduler.api.render.SheetRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleParseException;
import edu.zieit.scheduler.api.schedule.ScheduleLoader;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractScheduleLoader implements ScheduleLoader {

    protected final SheetRenderer renderer;

    public AbstractScheduleLoader(SheetRenderer renderer) {
        this.renderer = renderer;
    }

    protected Workbook loadWorkbook(ScheduleInfo info) throws ScheduleParseException {
        try (InputStream in = info.getUrl().openStream()) {
            String filename = info.getUrl().toString();
            Workbook workbook = null;

            if (filename.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(in);
            } else if (filename.endsWith(".xls")) {
                workbook = new HSSFWorkbook(in);
            }

            return workbook;
        } catch (IOException e) {
            throw new ScheduleParseException("Cannot load " + info.getUrl().toString(), e);
        }
    }

    protected Cell getCell(Sheet sheet, int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        return row != null ? row.getCell(colNum) : null;
    }

}
