package edu.zieit.scheduler.schedule;

import edu.zieit.scheduler.api.render.DocumentRenderException;
import edu.zieit.scheduler.api.render.DocumentRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.awt.image.BufferedImage;

public abstract class AbstractSchedule implements Schedule {

    private final ScheduleInfo info;
    private final Workbook workbook;
    private final Sheet sheet;
    private final DocumentRenderer documentRenderer;
    private BufferedImage documentImg;

    public AbstractSchedule(ScheduleInfo info, Sheet sheet, DocumentRenderer documentRenderer) throws IllegalArgumentException {
        this.info = info;
        this.workbook = sheet.getWorkbook();
        this.sheet = sheet;
        this.documentRenderer = documentRenderer;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public Sheet getSheet() {
        return sheet;
    }

    @Override
    public ScheduleInfo getInfo() {
        return info;
    }

    @Override
    public BufferedImage renderSheet() throws DocumentRenderException {
        if (documentImg == null) {
            documentImg = documentRenderer.render(this.sheet);
        }
        return documentImg;
    }
}
