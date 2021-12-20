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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class TimerService {

    private static final Logger logger = LogManager.getLogger(TimerService.class);

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

            logger.info("Course schedule reloaded. Users marked for mailing");
        }

        if (scheduleService.reloadTeacherSchedule()) {
            subsService.resetTeacherMailing();
            logger.info("Teacher schedule reloaded. Marked everyone for mailing");
        }

        if (scheduleService.reloadConsultSchedule()) {
            subsService.resetConsultMailing();
            logger.info("Consult schedule reloaded. Marked everyone for mailing");
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

        logger.info("Sent " + notMailed.size() + " messages during teachers mailing");
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
        logger.info("Sent " + notMailed.size() + " messages during courses mailing");
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
        logger.info("Sent " + notMailed.size() + " messages groups mailing");
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
        logger.info("Sent " + notMailed.size() + " messages consult mailing");
    }

    private void sendPhoto(String tgId, byte[] bytes, String message) {
        InputStream img = new ByteArrayInputStream(bytes);
        String filename = FilenameUtil.getNameWithExt(scheduleService, "photo");

        bot.send(SendDocument.builder()
                .chatId(tgId)
                .document(new InputFile(img, filename))
                .caption(message)
                .build());
    }
}
