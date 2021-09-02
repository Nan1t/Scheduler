package edu.zieit.scheduler.schedule.students;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * Schedule's class representation
 */
public class ScheduleClass {

    private final String name;
    private final String type;
    private final String teacher;
    private final String classroom;
    private final Collection<String> groups;

    public ScheduleClass(String name, String type, String teacher, String classroom, Collection<String> groups) {
        this.name = name;
        this.type = type;
        this.teacher = teacher;
        this.classroom = classroom;
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public Optional<String> getType() {
        return Optional.ofNullable(type);
    }

    public String getTeacher() {
        return teacher;
    }

    public Optional<String> getClassroom() {
        return Optional.ofNullable(classroom);
    }

    public Collection<String> getGroups() {
        return groups;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ScheduleClass) {
            ScheduleClass cl = (ScheduleClass) obj;
            return this.name.equals(cl.name)
                    && this.type.equals(cl.type)
                    && this.classroom.equals(cl.classroom)
                    && this.teacher.equals(cl.teacher)
                    && this.groups.equals(cl.groups);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, teacher, classroom, groups);
    }

    @Override
    public String toString() {
        return "Class{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", teacher='" + teacher + '\'' +
                ", classroom='" + classroom + '\'' +
                ", groups=" + groups +
                '}';
    }
}
