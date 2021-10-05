package edu.zieit.scheduler.api.schedule;

import java.util.Collection;
import java.util.Optional;

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

    /**
     * Load schedule with default way but try to file only first schedule in collection
     * @param info Schedule info
     * @return Loaded single schedule in Optional wrapper
     */
    default Optional<Schedule> loadSingle(final ScheduleInfo info) throws ScheduleParseException {
        return load(info).stream().findFirst();
    }
}
