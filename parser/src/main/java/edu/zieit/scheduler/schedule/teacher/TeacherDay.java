package edu.zieit.scheduler.schedule.teacher;

import java.util.*;

public class TeacherDay {

    private final String name;
    private final Map<Integer, TeacherClass> classes;

    public TeacherDay(String name, Map<Integer, TeacherClass> classes) {
        this.name = name;
        this.classes = classes;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, TeacherClass> getClasses() {
        return classes;
    }

    public boolean isEmpty() {
        return classes.isEmpty();
    }

    @Override
    public String toString() {
        return "TeacherDay{" +
                "name='" + name + '\'' +
                ", classes=" + classes +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name;
        private final Map<Integer, TeacherClass> classes;

        private Builder() {
            this.classes = new HashMap<>();
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder addCourses(int classNumber, TeacherClass teacherClass) {
            this.classes.putIfAbsent(classNumber, teacherClass);
            return this;
        }

        public TeacherDay build() {
            return new TeacherDay(name, classes);
        }

    }
}
