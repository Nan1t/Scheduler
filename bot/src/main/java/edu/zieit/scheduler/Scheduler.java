package edu.zieit.scheduler;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.config.MainConfig;
import edu.zieit.scheduler.config.ScheduleConfig;
import edu.zieit.scheduler.data.dao.TeacherSubsDao;
import edu.zieit.scheduler.data.subscription.SubscriptionPoints;
import edu.zieit.scheduler.data.subscription.SubscriptionStudent;
import edu.zieit.scheduler.data.subscription.SubscriptionTeacher;
import napi.configurate.yaml.lang.Language;
import napi.configurate.yaml.source.ConfigSources;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Scheduler {

    private MainConfig conf;
    private ScheduleConfig scheduleConf;
    private Language lang;

    private SessionFactory sessionFactory;

    public void start() throws Exception {
        Path rootDir = Paths.get("./");

        conf = new MainConfig(rootDir);
        scheduleConf = new ScheduleConfig(rootDir);
        lang = Language.builder()
                .source(ConfigSources.resource("/lang.yml", this)
                        .copyTo(rootDir))
                .build();

        conf.reload();
        scheduleConf.reload();
        lang.reload();

        initHibernate();

        TeacherSubsDao dao = new TeacherSubsDao(sessionFactory);
        SubscriptionTeacher sub = new SubscriptionTeacher();
        sub.setTelegramId("idtest");
        sub.setTeacher(Person.simple("First", "Last", "Part"));

        dao.create(sub);

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "Safe shutdown thread"));
    }

    public void shutdown() {

    }

    private void initHibernate() {
        Configuration configuration = new Configuration();

        configuration.addProperties(conf.getDbProperties());
        //configuration.addAnnotatedClass(SubscriptionPoints.class);
        configuration.addAnnotatedClass(SubscriptionTeacher.class);
        //configuration.addAnnotatedClass(SubscriptionStudent.class);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());

        sessionFactory = configuration.buildSessionFactory(builder.build());
    }
}
