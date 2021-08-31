package edu.zieit.scheduler.api.schedule;

import java.awt.image.BufferedImage;

/**
 * Implementors of this interface should render associated schedule to personal user's image
 */
public interface PersonalRenderer {

    /**
     * Get associated schedule
     * @return Schedule instance
     */
    Schedule getSchedule();

    /**
     * Render associated schedule to image
     * @return Rendered image
     */
    BufferedImage render();

}
