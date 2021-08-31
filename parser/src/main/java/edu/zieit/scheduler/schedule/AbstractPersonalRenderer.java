package edu.zieit.scheduler.schedule;

import edu.zieit.scheduler.api.schedule.PersonalRenderer;
import edu.zieit.scheduler.api.schedule.Schedule;

public abstract class AbstractPersonalRenderer implements PersonalRenderer {

    private final Schedule schedule;

    public AbstractPersonalRenderer(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public Schedule getSchedule() {
        return schedule;
    }
}
