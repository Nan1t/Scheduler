package parser;

import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.api.render.DocumentRenderException;
import edu.zieit.scheduler.api.render.DocumentRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleParser;
import edu.zieit.scheduler.schedule.students.StudentScheduleInfo;
import edu.zieit.scheduler.schedule.students.StudentScheduleParser;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.net.URL;

public class TestStudentsParser {

    @Test
    public void testParse() {
        URL url = getClass().getResource("/2k.xls");
        ScheduleInfo info = new StudentScheduleInfo(url, "name", new SheetPoint(0, 7), new SheetPoint(4, 5));
        ScheduleParser parser = new StudentScheduleParser(new EmptyRenderer());
        parser.parse(info);
    }

    private static class EmptyRenderer extends DocumentRenderer {
        @Override
        public BufferedImage render(Sheet sheet) throws DocumentRenderException {
            return null;
        }
    }

}
