package edu.zieit.scheduler.schedule;

import edu.zieit.scheduler.api.render.SheetRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleParseException;
import edu.zieit.scheduler.api.schedule.ScheduleLoader;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;

public abstract class AbstractScheduleLoader implements ScheduleLoader {

    protected final SheetRenderer renderer;

    public AbstractScheduleLoader(SheetRenderer renderer) {
        this.renderer = renderer;
    }

    protected Workbook loadWorkbook(ScheduleInfo info) throws ScheduleParseException {
        try (InputStream in = info.getUrl().openStream()) {
            return WorkbookFactory.create(in);
        } catch (IOException e) {
            throw new ScheduleParseException(e);
        }
    }

    protected Cell getCell(Sheet sheet, int rowNum, int colNum) {
        Row row = sheet.getRow(rowNum);
        return row != null ? row.getCell(colNum) : null;
    }

}
