package edu.zieit.scheduler.schedule;

import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.api.render.SheetRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import org.apache.poi.ss.usermodel.Sheet;

public abstract class AbstractSchedule implements Schedule {

    private final Sheet sheet;
    private final NamespacedKey key;
    private final SheetRenderer renderer;
    private byte[] image;

    public AbstractSchedule(ScheduleInfo info, Sheet sheet, SheetRenderer renderer) {
        this(NamespacedKey.of(info.getId()), sheet, renderer);
    }

    public AbstractSchedule(NamespacedKey key, Sheet sheet, SheetRenderer renderer) {
        this.sheet = sheet;
        this.key = key;
        this.renderer = renderer;
    }

    public AbstractSchedule(SheetRenderer renderer) {
        this.key = null;
        this.sheet = null;
        this.renderer = renderer;
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    public SheetRenderer getRenderer() {
        return renderer;
    }

    @Override
    public byte[] toImage() {
        if (image == null)
            image = renderer.renderBytes(sheet);

        return image;
    }

    @Override
    public ScheduleRenderer getPersonalRenderer(Object data, ScheduleService manager) {
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
