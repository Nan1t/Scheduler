package edu.zieit.scheduler.services;

import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.SchedulerBot;
import edu.zieit.scheduler.config.ScheduleConfig;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class TimerService {

    private final ScheduleConfig conf;
    private final SchedulerBot bot;
    private final ScheduleService scheduleService;
    private final SubsService subsService;

    private final ScheduledExecutorService timer;
    private ScheduledFuture<?> task;

    public TimerService(ScheduleConfig conf, SchedulerBot bot, ScheduleService scheduleService, SubsService subsService) {
        this.conf = conf;
        this.bot = bot;
        this.scheduleService = scheduleService;
        this.subsService = subsService;

        timer = Executors.newScheduledThreadPool(1);
    }

    public void start() {
        task = timer.scheduleWithFixedDelay(this::check, 0,
                conf.getCheckRate(), TimeUnit.SECONDS);
    }

    public void stop() {
        task.cancel(false);
        timer.shutdown();
    }

    private void check() {
        Collection<Schedule> reloaded = scheduleService.reloadCourseSchedule();

        if (!reloaded.isEmpty()) {
            sendCourses(reloaded);
        }

        if (scheduleService.reloadTeacherSchedule()) {
            sendTeachers();
        }

        if (scheduleService.reloadConsultSchedule()) {
            sendConsult();
        }
    }

    private void sendCourses(Collection<Schedule> schedules) {

    }

    private void sendTeachers() {

    }

    private void sendConsult() {

    }

}
