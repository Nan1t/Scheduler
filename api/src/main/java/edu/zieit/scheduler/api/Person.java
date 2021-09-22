package edu.zieit.scheduler.api;

import edu.zieit.scheduler.api.util.Levenshtein;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Person(String firstName, String lastName, String patronymic) {

    private static final Person EMPTY = simple(null, null, null);

    public static final int MAX_SIMILARITY = 1;

    public static Pattern REGEX_TEACHER = Pattern.compile("([А-ЯЁЇІЄҐ][а-яёїієґ']{0,32})\\s*([А-ЯЁЇІЄҐ])\\.\\s*([А-ЯЁЇІЄҐ])\\.*");
    public static Pattern REGEX_TEACHER_INLINE = Pattern.compile("([А-ЯЁЇІЄҐ][а-яёїієґ']{1,32}\\s*[А-ЯЁЇІЄҐ]\\.\\s*[А-ЯЁЇІЄҐ]\\.*)\\s*(ауд\\..{1,3})");

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
        return isEmpty() ? "NONE" : String.format("%s %s.%s.", lastName, firstName, patronymic);
    }

    public static Person empty() {
        return EMPTY;
    }

    public static Person simple(String firstName, String lastName, String patronymic) {
        return new Person(firstName, lastName, patronymic);
    }

    public static Person teacher(String source) {
        Matcher matcher = REGEX_TEACHER.matcher(source);
        return (matcher.find()) ? new Person(matcher.group(2), matcher.group(1), matcher.group(3)) : null;
    }
}
