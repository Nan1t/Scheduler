package parser;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleLoader;
import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import edu.zieit.scheduler.render.AsposeRenderer;
import edu.zieit.scheduler.schedule.consult.ConsultScheduleInfo;
import edu.zieit.scheduler.schedule.consult.ConsultScheduleLoader;
import napi.configurate.yaml.lang.Language;
import napi.configurate.yaml.source.ConfigSources;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.Paths;

public class TestConsultParser {

    @Test
    public void testParsing() {
        URL url = getClass().getResource("/consultation.xls");
        ScheduleInfo info = new ConsultScheduleInfo(url, new SheetPoint(1, 2), new SheetPoint(0, 3));
        ScheduleLoader loader = new ConsultScheduleLoader(new EmptyRenderer());

        for (Schedule schedule : loader.load(info)) {
            System.out.println(schedule);
        }
    }

    @Test
    public void testRender() throws Exception {
        TestScheduleManager manager = new TestScheduleManager();
        Language lang = Language.builder()
                .source(ConfigSources.resource("/lang.yml", this))
                .build();

        lang.reload();
        manager.setLang(lang);

        URL url = getClass().getResource("/consultation.xls");
        ScheduleInfo info = new ConsultScheduleInfo(url, new SheetPoint(1, 2), new SheetPoint(0, 3));
        ScheduleLoader loader = new ConsultScheduleLoader(new AsposeRenderer());
        Schedule schedule = loader.load(info).stream().findFirst().get();
        ScheduleRenderer renderer = schedule.getPersonalRenderer(Person.teacher("Костроміна О.Г."), manager);

        BufferedImage image = renderer.render();

        ImageIO.write(image, "jpeg", Paths.get("./test.jpeg").toFile());
    }

}
