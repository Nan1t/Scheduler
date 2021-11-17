package edu.zieit.scheduler;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.SchedulerBot;
import edu.zieit.scheduler.config.MainConfig;
import edu.zieit.scheduler.config.ScheduleConfig;
import edu.zieit.scheduler.persistence.ScheduleHash;
import edu.zieit.scheduler.persistence.TeacherNotice;
import edu.zieit.scheduler.persistence.dao.*;
import edu.zieit.scheduler.persistence.subscription.SubscriptionPoints;
import edu.zieit.scheduler.persistence.subscription.SubscriptionStudent;
import edu.zieit.scheduler.persistence.subscription.SubscriptionTeacher;
import edu.zieit.scheduler.services.ScheduleServiceImpl;
import edu.zieit.scheduler.services.SubsService;
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

        Person.REGEX_TEACHER = conf.getRegexTeacherDefault();
        Person.REGEX_TEACHER_INLINE = conf.getRegexTeacherInline();

        initHibernate(conf);

        TeacherSubsDao teacherDao = new TeacherSubsDao(sessionFactory);
        StudentSubsDao studentDao = new StudentSubsDao(sessionFactory);
        PointsSubsDao pointsDao = new PointsSubsDao(sessionFactory);
        NoticesDao noticesDao = new NoticesDao(sessionFactory);
        HashesDao hashesDao = new HashesDao(sessionFactory);

        SubsService subsService = new SubsService(teacherDao, studentDao, pointsDao, noticesDao);
        ScheduleService scheduleService = new ScheduleServiceImpl(lang, scheduleConf, hashesDao);

        logger.info("Loading schedule ...");
        scheduleService.reloadAll();
        logger.info("All schedule loaded");

        bot = new SchedulerBot(conf, lang, scheduleService, subsService);
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        logger.info("Starting long polling bot ...");
        botsApi.registerBot(bot);

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "Safe shutdown thread"));

        logger.info("Done! Scheduler ready to work");
    }

    public void shutdown() {
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
        configuration.addAnnotatedClass(SubscriptionStudent.class);
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
