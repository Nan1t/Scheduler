package edu.zieit.scheduler.inject;

import com.google.inject.AbstractModule;
import edu.zieit.scheduler.config.MainConfig;
import edu.zieit.scheduler.persistence.ScheduleHash;
import edu.zieit.scheduler.persistence.TeacherNotice;
import edu.zieit.scheduler.persistence.dao.*;
import edu.zieit.scheduler.persistence.subscription.*;
import edu.zieit.scheduler.persistence.webapi.ApiSession;
import edu.zieit.scheduler.persistence.webapi.ApiUser;
import edu.zieit.scheduler.util.LibLoader;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PersistenceModule extends AbstractModule {

    private final MainConfig conf;

    public PersistenceModule(MainConfig conf) {
        this.conf = conf;
    }

    @Override
    protected void configure() {
        SessionFactory sessionFactory = initHibernate();

        bind(SessionFactory.class).toInstance(sessionFactory);

        bind(TeacherSubsDao.class);
        bind(ConsultSubsDao.class);
        bind(CourseSubsDao.class);
        bind(GroupSubsDao.class);
        bind(PointsSubsDao.class);
        bind(NoticesDao.class);
        bind(HashesDao.class);
        bind(ApiUserDao.class);
        bind(ApiSessionDao.class);
    }

    private SessionFactory initHibernate() {
        Path driversDir = Paths.get("./drivers").toAbsolutePath();
        ClassLoader mainLoader = null;

        if (Files.exists(driversDir)) {
            LibLoader loader = new LibLoader();
            loader.loadAll(driversDir);
            loader.finishLoad();
            mainLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(loader.classLoader());
        }

        Configuration configuration = new Configuration();

        configuration.addProperties(conf.getDbProperties());

        configuration.addAnnotatedClass(SubscriptionPoints.class);
        configuration.addAnnotatedClass(SubscriptionTeacher.class);
        configuration.addAnnotatedClass(SubscriptionConsult.class);
        configuration.addAnnotatedClass(SubscriptionCourse.class);
        configuration.addAnnotatedClass(SubscriptionGroup.class);
        configuration.addAnnotatedClass(ScheduleHash.class);
        configuration.addAnnotatedClass(TeacherNotice.class);
        configuration.addAnnotatedClass(ApiUser.class);
        configuration.addAnnotatedClass(ApiSession.class);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());

        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());

        if (mainLoader != null)
            Thread.currentThread().setContextClassLoader(mainLoader);

        return sessionFactory;
    }

}
