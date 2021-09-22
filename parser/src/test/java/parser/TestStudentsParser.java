package parser;

import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.api.render.RenderException;
import edu.zieit.scheduler.api.render.SheetRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleLoader;
import edu.zieit.scheduler.schedule.students.StudentScheduleInfo;
import edu.zieit.scheduler.schedule.students.StudentScheduleLoader;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.net.URL;

public class TestStudentsParser {

    @Test
    public void testParse() {
        URL url = getClass().getResource("/2k.xls");
        ScheduleInfo info = new StudentScheduleInfo(url, "name", new SheetPoint(0, 7), new SheetPoint(4, 5));
        ScheduleLoader parser = new StudentScheduleLoader(new EmptyRenderer());
        parser.load(info);
    }

    private static class EmptyRenderer extends SheetRenderer {
        @Override
        public BufferedImage render(Sheet sheet) throws RenderException {
            return null;
        }
    }

}
