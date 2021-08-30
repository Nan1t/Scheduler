package edu.zieit.scheduler.api.schedule;

import java.awt.image.BufferedImage;

/**
 * Basic schedule interface
 */
public interface Schedule {

    /**
     * Get schedule basic info
     * @return Schedule info
     */
    ScheduleInfo getInfo();

    /**
     * Render static schedule's sheet source
     * @return Rendered image
     */
    BufferedImage renderSheet();

    /**
     * Get renderer for this schedule
     * @return Schedule renderer
     */
    ScheduleRenderer getRenderer();
}
