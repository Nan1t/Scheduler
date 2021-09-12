package edu.zieit.scheduler.schedule;

import edu.zieit.scheduler.api.render.DocumentRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import org.apache.poi.ss.usermodel.Sheet;

import java.awt.image.BufferedImage;

public abstract class AbstractSchedule implements Schedule {

    private final BufferedImage documentImg;

    public AbstractSchedule(Sheet sheet, DocumentRenderer documentRenderer) {
        this.documentImg = documentRenderer.render(sheet);
    }

    @Override
    public BufferedImage toImage() {
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
