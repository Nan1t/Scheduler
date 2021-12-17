package edu.zieit.scheduler.api;

import java.util.regex.Pattern;

public class Regexs {

    public static Pattern CLASSROOM = Pattern.compile("[а-яёїієґ']*\\.*\\s*([0-9]{3})");
    public static Pattern TEACHER = Pattern.compile("([А-ЯЁЇІЄҐ][а-яёїієґ']{0,32})\\s*([А-ЯЁЇІЄҐ])\\.\\s*([А-ЯЁЇІЄҐ])\\.*");
    public static Pattern TEACHER_INLINE = Pattern.compile("([А-ЯЁЇІЄҐ][а-яёїієґ']{1,32}\\s*[А-ЯЁЇІЄҐ]\\.\\s*[А-ЯЁЇІЄҐ]\\.*)\\s*(ауд\\..{1,3})");

}
