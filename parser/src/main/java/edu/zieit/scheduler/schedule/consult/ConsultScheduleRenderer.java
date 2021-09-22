package edu.zieit.scheduler.schedule.consult;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.render.RenderException;
import edu.zieit.scheduler.api.schedule.PersonalRenderer;

import java.awt.image.BufferedImage;

public class ConsultScheduleRenderer implements PersonalRenderer {

    private final ConsultSchedule schedule;
    private final Person teacher;

    public ConsultScheduleRenderer(ConsultSchedule schedule, Person teacher) {
        this.schedule = schedule;
        this.teacher = teacher;
    }

    @Override
    public BufferedImage render() throws RenderException {
        return null;
    }

}
