package edu.zieit.scheduler.schedule.teacher;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.render.RenderException;
import edu.zieit.scheduler.api.schedule.PersonalRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;

import java.awt.image.BufferedImage;
import java.util.Collection;

public class TeacherScheduleRenderer implements PersonalRenderer {

    private final TeacherSchedule schedule;
    private final Person person;
    private final Collection<Schedule> studentsSchedule;

    public TeacherScheduleRenderer(TeacherSchedule schedule, Person person, Collection<Schedule> studentsSchedule) {
        this.schedule = schedule;
        this.person = person;
        this.studentsSchedule = studentsSchedule;
    }

    @Override
    public BufferedImage render() throws RenderException {
        return null;
    }

}
