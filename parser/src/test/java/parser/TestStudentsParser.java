package parser;

import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.api.render.RenderException;
import edu.zieit.scheduler.api.render.SheetRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleLoader;
import edu.zieit.scheduler.schedule.students.StudentScheduleInfo;
import edu.zieit.scheduler.schedule.students.StudentScheduleLoader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

public class TestStudentsParser {

    @Test
    public void testParse() throws IOException {
        URL url = new URL("https://www.zieit.edu.ua/wp-content/uploads/Rozklad/1.xls");
        ScheduleInfo info = new StudentScheduleInfo(url, "Zaoch college",
                new SheetPoint(0, 7),
                new SheetPoint(4, 5));
        ScheduleLoader parser = new StudentScheduleLoader(new EmptyRenderer());
        Collection<Schedule> schedules = parser.load(info);

        for (Schedule schedule : schedules) {
            System.out.println(schedule);
        }
    }

}
