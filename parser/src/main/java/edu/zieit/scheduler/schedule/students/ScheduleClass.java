package edu.zieit.scheduler.schedule.students;

import edu.zieit.scheduler.api.Person;

import java.util.*;

/**
 * Schedule's class representation
 */
public class ScheduleClass {

    private final ScheduleDay day;
    private final String name;
    private final String type;
    private final Person teacher;
    private final String classroom;
    private final Collection<String> groups;

    private ScheduleClass(ScheduleDay day, String name, String type, Person teacher,
                          String classroom, Collection<String> groups) {
        this.day = day;
        this.name = name;
        this.type = type != null ? type : "";
        this.teacher = teacher;
        this.classroom = classroom != null ? classroom : "";
        this.groups = groups;
    }

    public ScheduleDay getDay() {
        return day;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Person getTeacher() {
        return teacher;
    }

    public String getClassroom() {
        return classroom;
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

    public static Builder builder(ScheduleDay day) {
        return new Builder(day);
    }

    public static class Builder {

        private final ScheduleDay day;
        private String name;
        private String type;
        private String teacher;
        private String classroom;
        private final Set<String> groups;

        private Builder(ScheduleDay day) {
            this.day = day;
            this.groups = new HashSet<>();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder teacher(String teacher) {
            this.teacher = teacher;
            return this;
        }

        public Builder classroom(String classroom) {
            this.classroom = classroom;
            return this;
        }

        public Builder group(String group) {
            this.groups.add(group);
            return this;
        }

        public ScheduleClass build() {
            return new ScheduleClass(day, name, type, Person.from(teacher), classroom, groups);
        }

    }
}
