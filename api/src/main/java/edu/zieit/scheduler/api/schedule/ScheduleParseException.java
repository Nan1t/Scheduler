package edu.zieit.scheduler.api.schedule;

public class ScheduleParseException extends RuntimeException {

    public ScheduleParseException(String message) {
        super(message);
    }

    public ScheduleParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScheduleParseException(Throwable cause) {
        super(cause);
    }
}
