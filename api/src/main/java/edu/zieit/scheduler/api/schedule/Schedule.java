package edu.zieit.scheduler.api.schedule;

import edu.zieit.scheduler.api.render.DocumentRenderException;

import java.awt.image.BufferedImage;

/**
 * Basic schedule interface
 */
public interface Schedule {

    /**
     * Get schedule info
     * @return Schedule info
     */
    ScheduleInfo getInfo();

    /**
     * Render static schedule's sheet source
     * @return Rendered image
     */
    BufferedImage renderSheet() throws DocumentRenderException;

    /**
     * Get renderer for this schedule
     * @return Schedule renderer
     */
    PersonalRenderer getPersonalRenderer();

}
