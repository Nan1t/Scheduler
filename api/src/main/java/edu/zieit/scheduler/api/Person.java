package edu.zieit.scheduler.api;

import edu.zieit.scheduler.api.util.Levenstain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Person(String firstName, String lastName, String patronymic) {

    public static final int MAX_SIMILARITY = 1;

    public static Pattern PATTERN_TEACHER = Pattern.compile("([А-ЯЁЇІЄҐ][а-яёїієґ']{0,32})\\s*([А-ЯЁЇІЄҐ])\\.([А-ЯЁЇІЄҐ])\\.*");
    public static Pattern PATTERN_TEACHER_INLINE = Pattern.compile("([А-ЯЁЇІЄҐ][а-яёїієґ']{1,32}\\s*[А-ЯЁЇІЄҐ]\\.[А-ЯЁЇІЄҐ]\\.*)\\s*(ауд\\..{1,3})");

    /**
     * Check is this person similar to another.
     * Two similar persons must have equals first name and patronymic
     * and Levenstain distance of last name less or equal that MAX_SIMILARITY parameter
     * @param another
     * @return
     */
    public boolean isSimilar(Person another) {
        int ln = Levenstain.calc(this.lastName(), another.lastName());
        return ln <= MAX_SIMILARITY
                && this.firstName().equals(another.firstName())
                && this.patronymic().equals(another.patronymic());
    }

    @Override
    public String toString() {
        return String.format("%s %s.%s.", lastName, firstName, patronymic);
    }

    public static Person simple(String firstName, String lastName, String patronymic) {
        return new Person(firstName, lastName, patronymic);
    }

    public static Person teacher(String source) {
        Matcher matcher = PATTERN_TEACHER.matcher(source);
        return (matcher.find()) ? new Person(matcher.group(2), matcher.group(1), matcher.group(3)) : null;
    }
}
