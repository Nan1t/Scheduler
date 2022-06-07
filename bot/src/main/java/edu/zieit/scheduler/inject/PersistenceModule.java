package edu.zieit.scheduler.inject;

import com.google.inject.AbstractModule;
import edu.zieit.scheduler.config.MainConfig;
import edu.zieit.scheduler.persistence.entity.ScheduleHash;
import edu.zieit.scheduler.persistence.dao.*;
import edu.zieit.scheduler.persistence.entity.*;
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

        bind(SubsTeacherDao.class);
        bind(SubsConsultDao.class);
        bind(SubsCourseDao.class);
        bind(SubsGroupDao.class);
        bind(SubsPointsDao.class);
        bind(HashesDao.class);
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

        configuration.addAnnotatedClass(SubsPoint.class);
        configuration.addAnnotatedClass(SubsTeacher.class);
        configuration.addAnnotatedClass(SubsConsult.class);
        configuration.addAnnotatedClass(SubsCourse.class);
        configuration.addAnnotatedClass(SubsGroup.class);
        configuration.addAnnotatedClass(ScheduleHash.class);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());

        SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());

        if (mainLoader != null)
            Thread.currentThread().setContextClassLoader(mainLoader);

        return sessionFactory;
    }

}
