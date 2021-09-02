package edu.zieit.scheduler.api.schedule;

import java.net.URL;

/**
 * Schedule basic info
 */
public interface ScheduleInfo {

    /**
     * Get schedule url
     * @return Schedule url
     */
    URL getUrl();

    /**
     * Get schedule id. Usually this is a name of file without extension.
     * @return Schedule id
     */
    String getId();
}
