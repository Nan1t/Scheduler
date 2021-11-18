package edu.zieit.scheduler.schedule.course;

import edu.zieit.scheduler.api.Person;

import java.util.*;

/**
 * Schedule's class representation
 */
public class CourseClass {

    private final String name;
    private final String type;
    private final Person person;
    private final String classroom;
    private final Collection<String> groups;

    private CourseClass(String name, String type, Person person,
                        String classroom, Collection<String> groups) {
        this.name = name;
        this.type = type != null ? type : "";
        this.person = person;
        this.classroom = classroom != null ? classroom : "";
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Person getTeacher() {
        return person;
    }

    public String getClassroom() {
        return classroom;
    }

    public Collection<String> getGroups() {
        return groups;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CourseClass cl) {
            return this.name.equals(cl.name)
                    && this.type.equals(cl.type)
                    && this.classroom.equals(cl.classroom)
                    && this.person.equals(cl.person)
                    && this.groups.equals(cl.groups);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, person, classroom, groups);
    }

    @Override
    public String toString() {
        return "Class{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", teacher='" + person + '\'' +
                ", classroom='" + classroom + '\'' +
                ", groups=" + groups +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private String type;
        private Person teacher;
        private String classroom;
        private final Set<String> groups;

        private Builder() {
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

        public Builder teacher(Person teacher) {
            this.teacher = teacher;
            return this;
        }

        public Builder teacher(String teacher) {
            this.teacher = Person.teacher(teacher);
            return this;
        }

        public Builder classroom(String classroom) {
            this.classroom = classroom;
            return this;
        }

        public Builder withGroup(String group) {
            this.groups.add(group);
            return this;
        }

        public CourseClass build() {
            return new CourseClass(name, type, teacher, classroom, groups);
        }

    }
}
