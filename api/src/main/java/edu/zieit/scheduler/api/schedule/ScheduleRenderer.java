package edu.zieit.scheduler.api.schedule;

import edu.zieit.scheduler.api.render.RenderException;

import java.awt.image.BufferedImage;

/**
 * Implementors of this interface should render associated schedule to personal user's image
 */
public interface ScheduleRenderer {

    /**
     * Render associated schedule to image
     * @return Rendered image
     */
    BufferedImage render() throws RenderException;

}
