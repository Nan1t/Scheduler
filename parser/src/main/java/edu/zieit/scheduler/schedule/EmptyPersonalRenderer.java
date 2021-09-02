package edu.zieit.scheduler.schedule;

import edu.zieit.scheduler.api.schedule.PersonalRenderer;

import java.awt.image.BufferedImage;

/**
 * Just empty renderer. Returns empty image instead of normal rendering
 */
public final class EmptyPersonalRenderer implements PersonalRenderer {

    private static final BufferedImage EMPTY_IMG;

    @Override
    public BufferedImage render() {
        return EMPTY_IMG;
    }

    static {
        EMPTY_IMG = new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
    }

}
