package edu.zieit.scheduler.schedule;

import edu.zieit.scheduler.api.schedule.Schedule;

import java.awt.image.BufferedImage;

/**
 * Just empty renderer. Returns empty image instead of normal rendering
 */
public final class EmptyPersonalRenderer extends AbstractPersonalRenderer {

    private static final BufferedImage EMPTY_IMG = new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);

    public EmptyPersonalRenderer(Schedule schedule) {
        super(schedule);
    }

    @Override
    public BufferedImage render() {
        return EMPTY_IMG;
    }

}
