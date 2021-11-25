package edu.zieit.scheduler.schedule;

import java.util.HashMap;
import java.util.Map;

public final class TimeTable {

    private static final Map<String, Integer> dayIndexes = new HashMap<>();
    private static final Map<Integer, String> timeTable = new HashMap<>();

    private TimeTable() {}

    public static int getDayIndex(String day) {
        return dayIndexes.getOrDefault(day.toLowerCase(), 0);
    }

    public static void setDayIndexes(Map<String, Integer> indexes) {
        for (var entry : indexes.entrySet()) {
            dayIndexes.put(entry.getKey().toLowerCase(), entry.getValue());
        }
    }

    public static String getTime(int classNum) {
        return timeTable.getOrDefault(classNum, "");
    }

    public static void addClassTime(int classNum, String time) {
        if (time != null) {
            timeTable.putIfAbsent(classNum, time);
        }
    }

}
