package edu.zieit.scheduler.schedule.students;

import java.util.*;

/**
 * Schedule's day representation.
 * Contains classes by their indexes.
 */
public class ScheduleDay {

    private final String name;
    private final String date;
    private final Map<Integer, String> timeTable;
    private final Map<Integer, List<ScheduleClass>> classes;

    private ScheduleDay(String name, String date, Map<Integer, String> timeTable,
                       Map<Integer, List<ScheduleClass>> classes) {
        this.name = name;
        this.date = date;
        this.timeTable = timeTable;
        this.classes = classes;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    /**
     * Get class beginning time by class index (1 - based)
     * @param classIndex Class index (1 - based)
     * @return Class beginning time or empty string if nothing found
     */
    public String getClassTime(int classIndex) {
        return timeTable.getOrDefault(classIndex, "");
    }

    /**
     * Get classes list relevant for specified index
     * @param classIndex Class index
     * @return List of schedule's classes
     */
    public List<ScheduleClass> getClasses(int classIndex) {
        return classes.getOrDefault(classIndex, Collections.emptyList());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private String date;
        private final Map<Integer, String> timeTable;
        private final Map<Integer, List<ScheduleClass>> classes;

        private Builder() {
            this.name = "";
            this.date = "";
            this.timeTable = new HashMap<>();
            this.classes = new HashMap<>();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder withTimePoint(int classIndex, String time) {
            timeTable.put(classIndex, time);
            return this;
        }

        public Builder withClass(int classIndex, ScheduleClass scheduleClass) {
            classes.computeIfAbsent(classIndex, (e) -> new ArrayList<>())
                    .add(scheduleClass);
            return this;
        }

        public ScheduleDay build() {
            return new ScheduleDay(name, date, timeTable, classes);
        }

    }

}
