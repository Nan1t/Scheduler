package edu.zieit.scheduler.schedule.teacher;

import edu.zieit.scheduler.schedule.TimeTable;

import java.util.*;

public class TeacherDay {

    private final int index;
    private final String name;
    private final Map<Integer, TeacherClass> classes;

    public TeacherDay(String name) {
        this.index = TimeTable.getDayIndex(name);
        this.name = name;
        this.classes = new HashMap<>();
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, TeacherClass> getClasses() {
        return classes;
    }

    public void addClass(int classNum, TeacherClass cl) {
        this.classes.putIfAbsent(classNum, cl);
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
}
