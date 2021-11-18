package edu.zieit.scheduler.schedule.course;

import edu.zieit.scheduler.api.Person;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Schedule's day representation.
 * Contains classes by their indexes.
 */
public class CourseDay {

    private final String name;
    private final String date;
    private final Map<Integer, List<CourseClass>> classes;

    private CourseDay(String name, String date, Map<Integer, List<CourseClass>> classes) {
        this.name = name;
        this.date = date;
        this.classes = classes;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Map<Integer, List<CourseClass>> getClasses() {
        return classes;
    }

    public Collection<CourseClass> getAllClasses() {
        Set<CourseClass> set = new LinkedHashSet<>();
        for (List<CourseClass> list : classes.values()) {
            set.addAll(list);
        }
        return set;
    }

    /**
     * Get classes list relevant for specified index
     * @param classIndex Class index
     * @return List of schedule's classes
     */
    public Collection<CourseClass> getClasses(int classIndex) {
        return classes.getOrDefault(classIndex, Collections.emptyList());
    }

    /**
     * Get classes list relevant for index and teacher
     * @param classIndex Class index
     * @param teacher Teacher person
     * @return Collection of classes
     */
    public Collection<CourseClass> getClasses(int classIndex, Person teacher) {
        return getClasses(classIndex)
                .stream()
                .filter(cl -> cl.getTeacher().equals(teacher))
                .collect(Collectors.toList());
    }

    public Optional<CourseClass> getGroupClass(int classIndex, String group) {
        return getClasses(classIndex)
                .stream()
                .filter(cl -> cl.getGroups().stream().anyMatch(group::equalsIgnoreCase))
                .findFirst();
    }

    @Override
    public String toString() {
        return "ScheduleDay{" +
                "name='" + name + '\'' +
                ", classes=" + classes +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private String date;
        private final Map<Integer, List<CourseClass>> classes;

        private Builder() {
            this.classes = new HashMap<>();
            date = "";
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder addClass(int classIndex, CourseClass courseClass) {
            classes.computeIfAbsent(classIndex, (e) -> new ArrayList<>())
                    .add(courseClass);
            return this;
        }

        public CourseDay build() {
            return new CourseDay(name, date, classes);
        }

    }

}
