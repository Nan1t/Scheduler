package edu.zieit.scheduler.schedule.students;

import java.util.*;

/**
 * Schedule's day representation.
 * Contains classes by their indexes.
 */
public class ScheduleDay {

    private final String name;
    private final Map<Integer, String> timeTable;
    private final Map<Integer, List<ScheduleClass>> classes;

    private ScheduleDay(String name, Map<Integer, String> timeTable,
                       Map<Integer, List<ScheduleClass>> classes) {
        this.name = name;
        this.timeTable = timeTable;
        this.classes = classes;
    }

    public String getName() {
        return name;
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
        private final Map<Integer, String> timeTable;
        private final Map<Integer, List<ScheduleClass>> classes;

        private Builder() {
            this.timeTable = new HashMap<>();
            this.classes = new HashMap<>();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder addTimePoint(int classIndex, String time) {
            timeTable.put(classIndex, time);
            return this;
        }

        public Builder addClass(int classIndex, ScheduleClass scheduleClass) {
            classes.computeIfAbsent(classIndex, (e) -> new ArrayList<>())
                    .add(scheduleClass);
            return this;
        }

        public ScheduleDay build() {
            return new ScheduleDay(name, timeTable, classes);
        }

    }

}
