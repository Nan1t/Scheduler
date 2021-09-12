package parser;

import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleParser;
import edu.zieit.scheduler.schedule.teacher.TeacherSchedule;
import edu.zieit.scheduler.schedule.teacher.TeacherScheduleInfo;
import edu.zieit.scheduler.schedule.teacher.TeacherScheduleParser;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Collection;
import java.util.Map;

public class TestTeacherParser {

    @Test
    public void testParse() throws Exception {
        URL url = getClass().getResource("/Teachers.xls");
        ScheduleInfo info = new TeacherScheduleInfo(url, Map.of());
        ScheduleParser parser = new TeacherScheduleParser(null);
        Collection<Schedule> schedules = parser.parse(info);

        for (Schedule schedule : schedules) {
            System.out.println(schedule.toString());

            if (schedule instanceof TeacherSchedule) {
                System.out.println(((TeacherSchedule) schedule).getTeachers());
            }
        }
    }

}
