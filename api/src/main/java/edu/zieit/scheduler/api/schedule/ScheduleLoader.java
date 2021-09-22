package edu.zieit.scheduler.api.schedule;

import java.util.Collection;

/**
 * Implementors of this interface load and parse schedule from url
 * and returns schedule instance with all parsed data
 */
public interface ScheduleLoader {

    /**
     * Load schedule
     * @param info Schedule info
     * @return Loaded schedule collection.
     * Sometimes this collection may contains several schedules
     */
    Collection<Schedule> load(final ScheduleInfo info) throws ScheduleParseException;

}
