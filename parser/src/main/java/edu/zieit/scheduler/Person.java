package edu.zieit.scheduler;

import java.util.Objects;

public class Person {

    private final String source;
    private final String minified;

    public Person(String source) {
        this.source = source;
        this.minified = minify(source);
    }

    public String getSource() {
        return source;
    }

    public String getMinified() {
        return minified;
    }

    private String minify(String source) {
        String src = source;
        src = src.replace(".", "");
        src = src.replace(" ", "");
        return src.toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Person) {
            Person person = (Person) o;
            return minified.equals(person.minified);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minified);
    }

    public static Person from(String source) {
        return new Person(source);
    }
}
