package edu.zieit.scheduler.schedule.teacher;

import java.util.*;

public class TeacherDay {

    private final String displayName;
    private final Map<Integer, Collection<String>> classes;

    public TeacherDay(String displayName, Map<Integer, Collection<String>> classes) {
        this.displayName = displayName;
        this.classes = classes;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Collection<String> getCourses(int classNumber) {
        return classes.getOrDefault(classNumber, Collections.emptyList());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String displayName;
        private final Map<Integer, Collection<String>> classes;

        private Builder() {
            this.classes = new HashMap<>();
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder addCourse(int classNumber, String course) {
            classes.computeIfAbsent(classNumber, (e)->new HashSet<>()).add(course);
            return this;
        }

        public Builder addCourses(int classNumber, String[] courses) {
            this.classes.computeIfAbsent(classNumber, (e)->new HashSet<>())
                    .addAll(Arrays.asList(courses));
            return this;
        }

        public TeacherDay build() {
            return new TeacherDay(displayName, classes);
        }

    }
}
