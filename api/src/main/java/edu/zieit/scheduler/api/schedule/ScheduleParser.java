package edu.zieit.scheduler.api.schedule;

/**
 * Implementors of this interface parses schedule from url
 * and returns parsed schedule instance
 */
public interface ScheduleParser {

    /**
     * Parse schedule
     * @param info Schedule info
     * @return Parsed schedule
     */
    Schedule parse(ScheduleInfo info) throws ScheduleParseException;

}
