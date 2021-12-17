package edu.zieit.scheduler.schedule.course;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.Regexs;

import java.util.*;
import java.util.regex.Matcher;

/**
 * Schedule's class representation
 */
public class CourseClass {

    private final int index;
    private final String name;
    private final Collection<String> groups;

    private String type;
    private Person person;
    private String classroom;

    public CourseClass(int index, String name) {
        this.index = index;
        this.name = name;
        this.type = "";
        this.classroom = "";
        this.groups = new ArrayList<>();
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? "" : type;
    }

    public Person getTeacher() {
        return person;
    }

    public void setTeacher(Person person) {
        this.person = person;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        String res = "";

        if (classroom != null) {
            try {
                Matcher matcher = Regexs.CLASSROOM.matcher(classroom);


                if (matcher.matches()) {
                    res = matcher.group(1);
                }
            } catch (Exception e) {
                // Something wrong, ignore
            }
        }

        this.classroom = res
                .replace(" ", "")
                .toLowerCase();
    }

    public Collection<String> getGroups() {
        return groups;
    }

    public void addGroup(String group) {
        groups.add(group);
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
}
