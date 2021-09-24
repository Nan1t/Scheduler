package edu.zieit.scheduler.schedule;

import edu.zieit.scheduler.api.NamespaceKey;
import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.render.SheetRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleManager;
import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import org.apache.poi.ss.usermodel.Sheet;

import java.awt.image.BufferedImage;

public abstract class AbstractSchedule implements Schedule {

    private final NamespaceKey key;
    private final BufferedImage documentImg;
    private final SheetRenderer renderer;

    public AbstractSchedule(ScheduleInfo info, Sheet sheet, SheetRenderer renderer) {
        this.key = NamespaceKey.of(info.getId());
        this.renderer = renderer;
        this.documentImg = renderer.render(sheet);
    }

    public AbstractSchedule(NamespaceKey key, Sheet sheet, SheetRenderer renderer) {
        this.key = key;
        this.renderer = renderer;
        this.documentImg = renderer.render(sheet);
    }

    @Override
    public NamespaceKey getKey() {
        return key;
    }

    public SheetRenderer getRenderer() {
        return renderer;
    }

    @Override
    public BufferedImage toImage() {
        return documentImg;
    }

    @Override
    public ScheduleRenderer getPersonalRenderer(Person person, ScheduleManager manager) {
        throw new RuntimeException("Renderer not implemented for this schedule type");
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
