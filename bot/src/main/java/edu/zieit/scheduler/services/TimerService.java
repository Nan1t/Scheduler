package edu.zieit.scheduler.services;

import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.SchedulerBot;
import edu.zieit.scheduler.config.ScheduleConfig;
import edu.zieit.scheduler.persistence.subscription.SubscriptionTeacher;
import edu.zieit.scheduler.util.FilenameUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
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
    private ScheduledFuture<?> checkTask;
    private ScheduledFuture<?> sendTask;

    public TimerService(ScheduleConfig conf, SchedulerBot bot, ScheduleService scheduleService, SubsService subsService) {
        this.conf = conf;
        this.bot = bot;
        this.scheduleService = scheduleService;
        this.subsService = subsService;
        this.timer = Executors.newScheduledThreadPool(2);
    }

    public void start() {
        checkTask = timer.scheduleWithFixedDelay(this::check, 0L,
                conf.getCheckRate(), TimeUnit.SECONDS);

        sendTask = timer.scheduleWithFixedDelay(this::mailSubscribers, 0L,
                1L, TimeUnit.SECONDS);
    }

    public void stop() {
        checkTask.cancel(false);
        sendTask.cancel(false);
        timer.shutdown();
    }

    private void check() {
        Collection<Schedule> reloaded = scheduleService.reloadCourseSchedule();

        if (!reloaded.isEmpty()) {
            // TODO reset who subscribed on these schedule
        }

        if (scheduleService.reloadTeacherSchedule()) {
            subsService.resetTeacherMailing();
        }

        if (scheduleService.reloadConsultSchedule()) {
            // TODO reset consult mailing status
        }
    }

    private void mailSubscribers() {
        sendTeachers();
    }

    private void sendTeachers() {
        Collection<SubscriptionTeacher> notMailed = subsService.getNotMailedTeacherSubs();
        List<SubscriptionTeacher> updated = new LinkedList<>();

        for (SubscriptionTeacher sub : notMailed) {
            ScheduleRenderer renderer = scheduleService.getTeacherSchedule()
                    .getPersonalRenderer(sub.getTeacher(), scheduleService);

            send(sub.getTelegramId(), renderer,
                    bot.getLang().of("mailing.teacher"));

            sub.setReceivedMailing(true);
            updated.add(sub);
        }

        if (updated.size() > 0)
            subsService.saveTeacherSubs(updated);
    }

    private void send(String tgId, ScheduleRenderer renderer, String message) {
        InputStream img = new ByteArrayInputStream(renderer.renderBytes());

        bot.send(SendPhoto.builder()
                .chatId(tgId)
                .photo(new InputFile(img, FilenameUtil.getNameWithExt(scheduleService, "photo")))
                .caption(message)
                .build());
    }
}
