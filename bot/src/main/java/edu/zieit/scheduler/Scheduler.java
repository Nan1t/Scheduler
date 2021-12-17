package edu.zieit.scheduler;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.Regexs;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.SchedulerBot;
import edu.zieit.scheduler.config.MainConfig;
import edu.zieit.scheduler.config.ScheduleConfig;
import edu.zieit.scheduler.persistence.ScheduleHash;
import edu.zieit.scheduler.persistence.TeacherNotice;
import edu.zieit.scheduler.persistence.dao.*;
import edu.zieit.scheduler.persistence.subscription.*;
import edu.zieit.scheduler.schedule.TimeTable;
import edu.zieit.scheduler.services.PointsService;
import edu.zieit.scheduler.services.ScheduleServiceImpl;
import edu.zieit.scheduler.services.SubsService;
import edu.zieit.scheduler.services.TimerService;
import napi.configurate.yaml.lang.Language;
import napi.configurate.yaml.source.ConfigSources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Scheduler {

    private static final Logger logger = LogManager.getLogger(Scheduler.class);

    private SessionFactory sessionFactory;
    private SchedulerBot bot;
    private TimerService timer;

    public void start() throws Exception {
        Path rootDir = Paths.get("./");

        MainConfig conf = new MainConfig(rootDir);
        ScheduleConfig scheduleConf = new ScheduleConfig(rootDir);
        Language lang = Language.builder()
                .source(ConfigSources.resource("/lang.yml", this)
                        .copyTo(rootDir))
                .build();

        conf.reload();
        scheduleConf.reload();
        lang.reload();

        Regexs.TEACHER = conf.getRegexTeacherDefault();
        Regexs.TEACHER_INLINE = conf.getRegexTeacherInline();
        Regexs.CLASSROOM = conf.getRegexClassroom();

        TimeTable.setDayIndexes(scheduleConf.getDayIndexes());

        initHibernate(conf);

        TeacherSubsDao teacherDao = new TeacherSubsDao(sessionFactory);
        ConsultSubsDao consultDao = new ConsultSubsDao(sessionFactory);
        CourseSubsDao coursesDao = new CourseSubsDao(sessionFactory);
        GroupSubsDao groupsDao = new GroupSubsDao(sessionFactory);
        PointsSubsDao pointsDao = new PointsSubsDao(sessionFactory);
        NoticesDao noticesDao = new NoticesDao(sessionFactory);
        HashesDao hashesDao = new HashesDao(sessionFactory);

        SubsService subsService = new SubsService(teacherDao, consultDao, coursesDao, pointsDao, noticesDao, groupsDao);
        ScheduleService scheduleService = new ScheduleServiceImpl(lang, scheduleConf, hashesDao);
        PointsService pointsService = new PointsService(conf);

        logger.info("Loading schedule ...");
        scheduleService.reloadAll();
        logger.info("All schedule loaded");

        bot = new SchedulerBot(conf, lang, scheduleService, subsService, pointsService);
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        logger.info("Starting long polling bot ...");
        botsApi.registerBot(bot);

        timer = new TimerService(scheduleConf, bot, scheduleService, subsService);
        timer.start();
        logger.info("Started timer");

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "Safe shutdown thread"));

        logger.info("Done! Scheduler ready to work");
    }

    public void shutdown() {
        logger.info("Stopping timer ...");
        timer.stop();
        logger.info("Closing connections ...");
        sessionFactory.close();
        logger.info("Closing long polling bot ...");
        bot.shutdown();
        System.out.println("Goodbye!");
    }

    private void initHibernate(MainConfig conf) {
        Configuration configuration = new Configuration();

        configuration.addProperties(conf.getDbProperties());

        configuration.addAnnotatedClass(SubscriptionPoints.class);
        configuration.addAnnotatedClass(SubscriptionTeacher.class);
        configuration.addAnnotatedClass(SubscriptionConsult.class);
        configuration.addAnnotatedClass(SubscriptionCourse.class);
        configuration.addAnnotatedClass(SubscriptionGroup.class);
        configuration.addAnnotatedClass(ScheduleHash.class);
        configuration.addAnnotatedClass(TeacherNotice.class);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());

        sessionFactory = configuration.buildSessionFactory(builder.build());
    }

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");

        try {
            new Scheduler().start();
        } catch (Throwable t) {
            logger.error("Cannot start scheduler", t);
        }
    }
}
