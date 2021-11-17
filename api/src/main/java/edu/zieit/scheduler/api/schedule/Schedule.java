package edu.zieit.scheduler.api.schedule;

import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.api.Person;

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
     * Get unique key of this schedule
     * @return Unique schedule key
     */
    NamespacedKey getKey();

    /**
     * Get rendered image
     * @return Rendered image
     */
    byte[] toImage();

    /**
     * Get personal schedule renderer
     * @param person Person for who renderer will be created
     * @param manager Schedule manager
     * @return Created personal schedule renderer
     */
    ScheduleRenderer getPersonalRenderer(Person person, ScheduleService manager);

}
