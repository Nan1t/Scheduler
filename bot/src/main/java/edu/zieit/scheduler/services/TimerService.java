package edu.zieit.scheduler.services;

import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.SchedulerBot;
import edu.zieit.scheduler.config.ScheduleConfig;
import edu.zieit.scheduler.persistence.subscription.SubscriptionConsult;
import edu.zieit.scheduler.persistence.subscription.SubscriptionCourse;
import edu.zieit.scheduler.persistence.subscription.SubscriptionGroup;
import edu.zieit.scheduler.persistence.subscription.SubscriptionTeacher;
import edu.zieit.scheduler.schedule.course.CourseSchedule;
import edu.zieit.scheduler.util.FilenameUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
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
            List<NamespacedKey> keys = reloaded.stream()
                    .map(Schedule::getKey)
                    .toList();

            Set<String> groups = new HashSet<>();

            for (Schedule schedule : reloaded) {
                if (schedule instanceof CourseSchedule course) {
                    groups.addAll(course.getGroupNames());
                }
            }

            subsService.resetCourseMailing(keys);
            subsService.resetGroupMailing(groups);
        }

        if (scheduleService.reloadTeacherSchedule()) {
            subsService.resetTeacherMailing();
        }

        if (scheduleService.reloadConsultSchedule()) {
            subsService.resetConsultMailing();
        }
    }

    private void mailSubscribers() {
        sendTeachers();
        sendCourses();
        sendGroups();
        sendConsult();
    }

    private void sendTeachers() {
        Collection<SubscriptionTeacher> notMailed = subsService.getNotMailedTeacherSubs();

        for (SubscriptionTeacher sub : notMailed) {
            ScheduleRenderer renderer = scheduleService.getTeacherSchedule()
                    .getPersonalRenderer(sub.getTeacher(), scheduleService);

            sendPhoto(sub.getTelegramId(), renderer.renderBytes(),
                    bot.getLang().of("mailing.teacher"));

            sub.setReceivedMailing(true);
        }

        subsService.updateTeacherSubs(notMailed);
    }

    private void sendCourses() {
        Collection<SubscriptionCourse> notMailed = subsService.getNotMailedCourseSubs();

        for (SubscriptionCourse sub : notMailed) {
            Schedule schedule = scheduleService.getCourseSchedule(sub.getScheduleKey());

            if (schedule != null) {
                sendPhoto(sub.getTelegramId(), schedule.toImage(),
                        bot.getLang().of("mailing.course"));
            }

            sub.setReceivedMailing(true);
        }

        subsService.updateCourseSubs(notMailed);
    }

    private void sendGroups() {
        Collection<SubscriptionGroup> notMailed = subsService.getNotMailedGroupSubs();

        for (SubscriptionGroup sub : notMailed) {
            Optional<Schedule> schedule = scheduleService.getCourseByGroup(sub.getGroupName());

            if (schedule.isPresent()) {
                ScheduleRenderer renderer = schedule.get().getPersonalRenderer(sub.getGroupName(), scheduleService);

                sendPhoto(sub.getTelegramId(), renderer.renderBytes(),
                        bot.getLang().of("mailing.group"));
            }

            sub.setReceivedMailing(true);
        }

        subsService.updateGroupSubs(notMailed);
    }

    private void sendConsult() {
        Collection<SubscriptionConsult> notMailed = subsService.getNotMailedConsultSubs();

        for (SubscriptionConsult sub : notMailed) {
            ScheduleRenderer renderer = scheduleService.getConsultSchedule()
                    .getPersonalRenderer(sub.getTeacher(), scheduleService);

            sendPhoto(sub.getTelegramId(), renderer.renderBytes(),
                    bot.getLang().of("mailing.consult"));

            sub.setReceivedMailing(true);
        }

        subsService.updateConsultSubs(notMailed);
    }

    private void sendPhoto(String tgId, byte[] bytes, String message) {
        InputStream img = new ByteArrayInputStream(bytes);
        String filename = FilenameUtil.getNameWithExt(scheduleService, "photo");

        bot.send(SendPhoto.builder()
                .chatId(tgId)
                .photo(new InputFile(img, filename))
                .caption(message)
                .build());
    }
}
