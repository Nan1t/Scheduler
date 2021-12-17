package edu.zieit.scheduler.api;

import edu.zieit.scheduler.api.util.Levenshtein;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Matcher;

public record Person(String firstName, String lastName, String patronymic) implements Serializable {

    private static final Person EMPTY = simple(null, null, null);

    public static final int MAX_SIMILARITY = 1;

    /**
     * Check is this person similar to another.
     * Two similar persons must have equals first name and patronymic
     * and Levenshtein distance of last name less or equal that MAX_SIMILARITY parameter
     * @param another another Person
     * @return true is this Person similar to another or false otherwise
     */
    public boolean isSimilar(Person another) {
        if (isEmpty())
            return false;

        int ln = Levenshtein.calcDistance(this.lastName(), another.lastName());
        return ln <= MAX_SIMILARITY
                && this.firstName().equals(another.firstName())
                && this.patronymic().equals(another.patronymic());
    }

    public boolean isEmpty() {
        return firstName == null && lastName == null && patronymic == null;
    }

    @Override
    public String toString() {
        return isEmpty() ? "" : String.format("%s %s.%s.", lastName, firstName, patronymic);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(firstName, person.firstName)
                && Objects.equals(lastName, person.lastName)
                && Objects.equals(patronymic, person.patronymic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, patronymic);
    }

    public static Person empty() {
        return EMPTY;
    }

    public static Person simple(String firstName, String lastName, String patronymic) {
        return new Person(firstName, lastName, patronymic);
    }

    public static Person teacher(String source) {
        if (source == null) return null;
        Matcher matcher = Regexs.TEACHER.matcher(source);
        return (matcher.find()) ? new Person(matcher.group(2), matcher.group(1), matcher.group(3)) : null;
    }
}
