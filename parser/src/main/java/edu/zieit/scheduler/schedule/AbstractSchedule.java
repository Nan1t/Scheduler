package edu.zieit.scheduler.schedule;

import edu.zieit.scheduler.api.render.DocumentRenderException;
import edu.zieit.scheduler.api.render.DocumentRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.awt.image.BufferedImage;

public abstract class AbstractSchedule implements Schedule {

    private final Workbook workbook;
    private final Sheet sheet;
    private final DocumentRenderer documentRenderer;
    private BufferedImage documentImg;

    public AbstractSchedule(Sheet sheet, DocumentRenderer documentRenderer) throws IllegalArgumentException {
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
    public BufferedImage renderSheet() throws DocumentRenderException {
        if (documentImg == null) {
            documentImg = documentRenderer.render(this.sheet);
        }
        return documentImg;
    }

    @Override
    public int hashCode() {
        return getInfo().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractSchedule) {
            return getInfo().equals(obj);
        }
        return false;
    }
}
