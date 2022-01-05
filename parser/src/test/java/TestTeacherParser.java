import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleParseException;
import edu.zieit.scheduler.render.AsposeRenderer;
import edu.zieit.scheduler.schedule.teacher.TeacherScheduleInfo;
import edu.zieit.scheduler.schedule.teacher.TeacherScheduleLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Map;

public class TestTeacherParser {

    @Test
    public void testParse() throws Exception {
        URL url = new URL("https://www.zieit.edu.ua/wp-content/uploads/Rozklad/Teachers.xls");
        ScheduleInfo info = new TeacherScheduleInfo(url, Map.of());
        TeacherScheduleLoader loader = new TeacherScheduleLoader(new AsposeRenderer());
        Assertions.assertThrows(ScheduleParseException.class, ()->loader.load(info));
    }

}
