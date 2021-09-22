package edu.zieit.scheduler.schedule;

import edu.zieit.scheduler.api.render.SheetRenderer;
import org.apache.poi.ss.usermodel.Sheet;

public abstract class AbstractScheduleBuilder {

    private final Sheet sheet;
    private final SheetRenderer renderer;

    public AbstractScheduleBuilder(Sheet sheet, SheetRenderer renderer) {
        this.sheet = sheet;
        this.renderer = renderer;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public SheetRenderer getRenderer() {
        return renderer;
    }
}
