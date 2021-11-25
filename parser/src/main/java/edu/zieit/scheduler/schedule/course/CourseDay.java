package edu.zieit.scheduler.schedule.course;

import edu.zieit.scheduler.api.Person;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Schedule's day representation.
 * Contains classes by their indexes.
 */
public class CourseDay {

    private String name;
    private String date;
    private final Map<Integer, List<CourseClass>> classes;

    public CourseDay() {
        this.name = "";
        this.date = "";
        this.classes = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date == null ? "" : date;
    }

    public Map<Integer, List<CourseClass>> getClasses() {
        return classes;
    }

    public void addClass(int classIndex, CourseClass courseClass) {
        classes.computeIfAbsent(classIndex, (e) -> new ArrayList<>())
                .add(courseClass);
    }

    public Collection<CourseClass> getClasses(int classIndex) {
        return classes.getOrDefault(classIndex, Collections.emptyList());
    }

    public Collection<CourseClass> getClasses(int classIndex, Person teacher) {
        return getClasses(classIndex)
                .stream()
                .filter(cl -> cl.getTeacher().equals(teacher))
                .collect(Collectors.toList());
    }

    public Collection<CourseClass> getAllClasses() {
        Set<CourseClass> set = new LinkedHashSet<>();
        for (List<CourseClass> list : classes.values()) {
            set.addAll(list);
        }
        return set;
    }

    public Optional<CourseClass> getGroupClass(int classIndex, String group) {
        return getClasses(classIndex)
                .stream()
                .filter(cl -> cl.getGroups().stream().anyMatch(group::equals))
                .findFirst();
    }

    @Override
    public String toString() {
        return "ScheduleDay{" +
                "name='" + name + '\'' +
                ", classes=" + classes +
                '}';
    }

}
