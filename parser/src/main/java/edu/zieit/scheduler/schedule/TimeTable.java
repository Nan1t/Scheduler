package edu.zieit.scheduler.schedule;

import java.util.HashMap;
import java.util.Map;

public final class TimeTable {

    private static final Map<Integer, String> timeTable = new HashMap<>();

    private TimeTable() {}

    public static String getTime(int classNum) {
        return timeTable.getOrDefault(classNum, "");
    }

    public static void addClassTime(int classNum, String time) {
        if (time != null) {
            timeTable.putIfAbsent(classNum, time);
        }
    }

}
