package parser;

import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.api.schedule.*;
import edu.zieit.scheduler.render.AsposeRenderer;
import edu.zieit.scheduler.schedule.course.CourseScheduleInfo;
import edu.zieit.scheduler.schedule.course.CourseScheduleLoader;
import edu.zieit.scheduler.schedule.teacher.TeacherSchedule;
import edu.zieit.scheduler.schedule.teacher.TeacherScheduleInfo;
import edu.zieit.scheduler.schedule.teacher.TeacherScheduleLoader;
import napi.configurate.yaml.lang.Language;
import napi.configurate.yaml.source.ConfigSources;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class TestTeacherParser {

    private Collection<Schedule> parse() {
        URL url = getClass().getResource("/Teachers.xls");
        Map<String, NamespacedKey> associations = new HashMap<>();

        associations.put("1", NamespacedKey.of("1"));
        associations.put("2", NamespacedKey.of("2"));
        associations.put("3", NamespacedKey.of("3"));
        associations.put("4", NamespacedKey.of("4"));
        associations.put("5", NamespacedKey.of("5"));
        associations.put("к9", NamespacedKey.of("1k"));
        associations.put("кол", NamespacedKey.of("2k"));
        associations.put("кол3", NamespacedKey.of("3k"));
        associations.put("кол4", NamespacedKey.of("4k"));
        associations.put("1(з)", NamespacedKey.of("zo", "1 курс"));
        associations.put("2(з)", NamespacedKey.of("zo", "2 курс"));
        associations.put("3(з)", NamespacedKey.of("zo", "3 курс"));
        associations.put("4(з)", NamespacedKey.of("zo", "4 курс"));
        associations.put("5(з)", NamespacedKey.of("zo", "5 курс"));
        associations.put("1к(з)", NamespacedKey.of("zc", "1 курс"));
        associations.put("2к(з)", NamespacedKey.of("zc", "2 курс"));
        associations.put("1,2,3 курс", NamespacedKey.of("do", "1,2,3 курс"));
        associations.put("магістратура", NamespacedKey.of("zo", "магістратура"));

        ScheduleInfo info = new TeacherScheduleInfo(url, associations);
        ScheduleLoader parser = new TeacherScheduleLoader(new AsposeRenderer());
        return parser.load(info);
    }

    private Map<NamespacedKey, Schedule> parseStudents() {
        String[] paths = {"1.xls", "2.xls", "3.xls", "4.xls", "5.xls", "1k.xls", "2k.xls", "3k.xls", "4k.xls", "zc.xlsx", "zo.xlsx", "do.xls"};
        Map<NamespacedKey, Schedule> scheduleMap = new HashMap<>();

        for (String str : paths) {
            URL url = getClass().getResource("/" + str);

            ScheduleInfo info = new CourseScheduleInfo(url, "Schedule " + str,
                    new SheetPoint(0, 7),
                    new SheetPoint(4, 5));
            ScheduleLoader parser = new CourseScheduleLoader(new EmptyRenderer());

            for (Schedule schedule : parser.load(info)) {
                scheduleMap.put(schedule.getKey(), schedule);
                System.out.println("Loaded " + schedule.getKey());
            }

        }

        return scheduleMap;
    }

    @Test
    public void testParse() throws Exception {
        Collection<Schedule> schedules = parse();

        for (Schedule schedule : schedules) {
            System.out.println(schedule.toString());

            if (schedule instanceof TeacherSchedule) {
                System.out.println(((TeacherSchedule) schedule).getTeachers());
            }
        }
    }

    @Test
    public void testRender() throws Exception {
        TestScheduleService manager = new TestScheduleService();
        Language lang = Language.builder()
                .source(ConfigSources.resource("/lang.yml", this))
                .build();

        lang.reload();

        Collection<Schedule> schedules = parse();
        Schedule teachers = schedules.stream().findFirst().get();

        manager.setLang(lang);
        manager.setStudentSchedule(parseStudents());
        manager.setTeacherSchedule(teachers);

        ScheduleRenderer renderer = teachers.getPersonalRenderer(Person.teacher("Резніченко Ю.С."), manager);
        BufferedImage image = renderer.render();

        ImageIO.write(image, "jpeg", Paths.get("./test.jpeg").toFile());
    }

}
