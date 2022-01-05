import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleParseException;
import edu.zieit.scheduler.render.AsposeRenderer;
import edu.zieit.scheduler.schedule.course.CourseScheduleInfo;
import edu.zieit.scheduler.schedule.course.CourseScheduleLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;

public class TestCourseParser {

    @Test
    public void testParse() throws Exception {
        URL url = new URL("https://www.zieit.edu.ua/wp-content/uploads/Rozklad/1.xls");
        ScheduleInfo info = new CourseScheduleInfo(url, "Test course",
                new SheetPoint(0, 0), new SheetPoint(0, 0));
        CourseScheduleLoader loader = new CourseScheduleLoader(new AsposeRenderer());
        Assertions.assertThrows(ScheduleParseException.class, ()->loader.load(info));
    }

}
