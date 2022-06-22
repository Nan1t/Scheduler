package edu.zieit.scheduler.util;

public final class TimeUtil {

    private TimeUtil() {}

    public static long currentUnixTime() {
        return System.currentTimeMillis() / 1000L;
    }

}
