package edu.zieit.scheduler.services;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.Bot;
import edu.zieit.scheduler.config.ScheduleConfig;
import edu.zieit.scheduler.persistence.entity.*;
import edu.zieit.scheduler.schedule.course.CourseSchedule;
import edu.zieit.scheduler.util.FilenameUtil;
import edu.zieit.scheduler.api.config.Language;
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

    private final Bot bot;
    private final Language lang;
    private final ScheduleConfig conf;
    private final ScheduleService scheduleService;
    private final SubsService subsService;

    private final ScheduledExecutorService timer;
    private ScheduledFuture<?> checkTask;
    private ScheduledFuture<?> sendTask;

    @Inject
    public TimerService(
            Bot bot,
            Language lang,
            ScheduleConfig conf,
            ScheduleService scheduleService,
            SubsService subsService
    ) {
        this.bot = bot;
        this.lang = lang;
        this.conf = conf;
        this.scheduleService = scheduleService;
        this.subsService = subsService;
        this.timer = Executors.newScheduledThreadPool(2);
    }

    public void start() {
        checkTask = timer.scheduleWithFixedDelay(this::check, 0L,
                conf.getCheckRate(), TimeUnit.SECONDS);

        sendTask = timer.scheduleWithFixedDelay(this::mailSubscribers, 0L,
                2L, TimeUnit.SECONDS);
    }

    public void stop() {
        checkTask.cancel(false);
        sendTask.cancel(false);
        timer.shutdown();
    }

    private void check() {
        Collection<Schedule> reloaded = scheduleService.reloadCourseSchedule(false);

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

            subsService.resetCourseNotifications(keys);
            subsService.resetGroupNotifications(groups);

            logger.info("Course schedule reloaded. Users marked for mailing");
        }

        if (scheduleService.reloadTeacherSchedule(false)) {
            subsService.resetTeacherNotifications();
            logger.info("Teacher schedule reloaded. Marked everyone for mailing");
        }

        if (scheduleService.reloadConsultSchedule(false)) {
            subsService.resetConsultNotifications();
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
        Collection<SubsTeacher> notMailed = subsService.getNotNotifiedTeacherSubs();

        if (!notMailed.isEmpty()) {
            for (SubsTeacher sub : notMailed) {
                Person teacher = Person.simple(
                        sub.getFistName(), 
                        sub.getLastName(), 
                        sub.getPatronymic()
                );
                ScheduleRenderer renderer = scheduleService.getTeacherSchedule()
                        .getPersonalRenderer(teacher, scheduleService);

                sendPhoto(sub.getTgId(), renderer.renderBytes(), lang.of("mailing.teacher"));

                sub.setNotified(true);
            }

            subsService.updateTeacherSubs(notMailed);

            logger.info("Sent " + notMailed.size() + " messages during teachers mailing");
        }
    }

    private void sendCourses() {
        Collection<SubsCourse> notMailed = subsService.getNotNotifiedCourseSubs();

        if (!notMailed.isEmpty()) {
            for (SubsCourse sub : notMailed) {
                Schedule schedule = scheduleService.getCourseSchedule(sub.getScheduleKey());

                if (schedule != null) {
                    sendPhoto(sub.getTgId(), schedule.toImage(), lang.of("mailing.course"));
                }

                sub.setNotified(true);
            }

            subsService.updateCourseSubs(notMailed);
            logger.info("Sent " + notMailed.size() + " messages during courses mailing");
        }
    }

    private void sendGroups() {
        Collection<SubsGroup> notMailed = subsService.getNotNotifiedGroupSubs();

        if (!notMailed.isEmpty()) {
            for (SubsGroup sub : notMailed) {
                Optional<Schedule> schedule = scheduleService.getCourseByGroup(sub.getGroupName());

                if (schedule.isPresent()) {
                    ScheduleRenderer renderer = schedule.get().getPersonalRenderer(sub.getGroupName(), scheduleService);

                    sendPhoto(sub.getTgId(), renderer.renderBytes(), lang.of("mailing.group"));
                }

                sub.setNotified(true);
            }

            subsService.updateGroupSubs(notMailed);
            logger.info("Sent " + notMailed.size() + " messages groups mailing");
        }
    }

    private void sendConsult() {
        Collection<SubsConsult> notMailed = subsService.getNotNotifiedConsultSubs();

        if (!notMailed.isEmpty()) {
            for (SubsConsult sub : notMailed) {
                Person teacher = Person.simple(
                        sub.getFistName(),
                        sub.getLastName(),
                        sub.getPatronymic()
                );
                ScheduleRenderer renderer = scheduleService.getConsultSchedule()
                        .getPersonalRenderer(teacher, scheduleService);

                sendPhoto(sub.getTgId(), renderer.renderBytes(), lang.of("mailing.consult"));

                sub.setNotified(true);
            }

            subsService.updateConsultSubs(notMailed);
            logger.info("Sent " + notMailed.size() + " messages during consult mailing");
        }
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
