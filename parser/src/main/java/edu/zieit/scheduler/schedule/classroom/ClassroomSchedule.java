package edu.zieit.scheduler.schedule.classroom;

import edu.zieit.scheduler.api.schedule.ScheduleInfo;
import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.schedule.AbstractSchedule;
import edu.zieit.scheduler.schedule.course.CourseDay;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClassroomSchedule extends AbstractSchedule {

    private final Map<Integer, CourseDay> days;

    public ClassroomSchedule() {
        super(null);
        this.days = new HashMap<>();
    }

    @Override
    public ScheduleInfo getInfo() {
        return null;
    }

    public Map<Integer, CourseDay> getDays() {
        return days;
    }

    public void addDay(int dayIndex, CourseDay classroomDay) {
        days.put(dayIndex, classroomDay);
    }

    @Override
    public ScheduleRenderer getPersonalRenderer(Object data, ScheduleService manager) {
        return new ClassroomRenderer(data.toString(), this, manager);
    }
}
