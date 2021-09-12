package edu.zieit.scheduler.api.schedule;

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
     * Get rendered image
     * @return Rendered image
     */
    BufferedImage toImage();

}
