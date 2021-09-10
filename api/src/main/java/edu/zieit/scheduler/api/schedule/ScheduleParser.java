package edu.zieit.scheduler.api.schedule;

import java.util.Collection;

/**
 * Implementors of this interface parses schedule from url
 * and returns parsed schedule instance
 */
public interface ScheduleParser {

    /**
     * Parse schedule
     * @param info Schedule info
     * @return Parsed schedule collection.
     * Sometimes this collection may contains several schedules
     */
    Collection<Schedule> parse(final ScheduleInfo info) throws ScheduleParseException;

}
