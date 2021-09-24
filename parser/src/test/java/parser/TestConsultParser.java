package parser;

import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleLoader;
import edu.zieit.scheduler.schedule.consult.ConsultScheduleInfo;
import edu.zieit.scheduler.schedule.consult.ConsultScheduleLoader;
import org.junit.jupiter.api.Test;

import java.net.URL;

public class TestConsultParser {

    @Test
    public void testParsing() {
        URL url = getClass().getResource("/consultation.xls");
        ScheduleInfo info = new ConsultScheduleInfo(url, new SheetPoint(2, 3), new SheetPoint(1, 6));
        ScheduleLoader loader = new ConsultScheduleLoader(new EmptyRenderer());

        for (Schedule schedule : loader.load(info)) {
            System.out.println(schedule);
        }
    }

}
