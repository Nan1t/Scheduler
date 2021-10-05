package edu.zieit.scheduler;

import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.config.MainConfig;
import edu.zieit.scheduler.config.ScheduleConfig;
import edu.zieit.scheduler.persistence.ScheduleHash;
import edu.zieit.scheduler.persistence.dao.PointsSubsDao;
import edu.zieit.scheduler.persistence.dao.ScheduleHashesDao;
import edu.zieit.scheduler.persistence.dao.StudentSubsDao;
import edu.zieit.scheduler.persistence.dao.TeacherSubsDao;
import edu.zieit.scheduler.persistence.subscription.SubscriptionPoints;
import edu.zieit.scheduler.persistence.subscription.SubscriptionStudent;
import edu.zieit.scheduler.persistence.subscription.SubscriptionTeacher;
import edu.zieit.scheduler.services.ScheduleServiceImpl;
import napi.configurate.yaml.lang.Language;
import napi.configurate.yaml.source.ConfigSources;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Scheduler {

    private SessionFactory sessionFactory;

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

        initHibernate(conf);

        TeacherSubsDao teacherDao = new TeacherSubsDao(sessionFactory);
        StudentSubsDao studentDao = new StudentSubsDao(sessionFactory);
        PointsSubsDao pointsDao = new PointsSubsDao(sessionFactory);
        ScheduleHashesDao hashesDao = new ScheduleHashesDao(sessionFactory);

        ScheduleService scheduleService = new ScheduleServiceImpl(lang, scheduleConf, hashesDao);

        scheduleService.reloadAll();

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "Safe shutdown thread"));
    }

    public void shutdown() {

    }

    private void initHibernate(MainConfig conf) {
        Configuration configuration = new Configuration();

        configuration.addProperties(conf.getDbProperties());

        configuration.addAnnotatedClass(SubscriptionPoints.class);
        configuration.addAnnotatedClass(SubscriptionTeacher.class);
        configuration.addAnnotatedClass(SubscriptionStudent.class);
        configuration.addAnnotatedClass(ScheduleHash.class);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());

        sessionFactory = configuration.buildSessionFactory(builder.build());
    }
}
