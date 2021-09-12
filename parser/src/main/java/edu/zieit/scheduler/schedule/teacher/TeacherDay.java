package edu.zieit.scheduler.schedule.teacher;

import java.util.*;

public class TeacherDay {

    private final int index;
    private final Map<Integer, Collection<String>> classes;

    public TeacherDay(int index, Map<Integer, Collection<String>> classes) {
        this.index = index;
        this.classes = classes;
    }

    public int getIndex() {
        return index;
    }

    public Collection<String> getCourses(int classNumber) {
        return classes.getOrDefault(classNumber, Collections.emptyList());
    }

    public boolean isEmpty() {
        return classes.isEmpty();
    }

    @Override
    public String toString() {
        return "TeacherDay{" +
                "index='" + index + '\'' +
                ", classes=" + classes +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private int index;
        private final Map<Integer, Collection<String>> classes;

        private Builder() {
            this.classes = new HashMap<>();
        }

        public Builder index(int index) {
            this.index = index;
            return this;
        }

        public Builder addCourses(int classNumber, Collection<String> courses) {
            this.classes.computeIfAbsent(classNumber, (e)->new HashSet<>())
                    .addAll(courses);
            return this;
        }

        public TeacherDay build() {
            return new TeacherDay(index, classes);
        }

    }
}
