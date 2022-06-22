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
import java.util.function.Supplier;

public class PersistenceModule extends AbstractModule {

    private final MainConfig conf;

    public PersistenceModule(MainConfig conf) {
        this.conf = conf;
    }

    @Override
    protected void configure() {
        SessionFactory sessionFactory = initHibernate();

        bind(SessionFactory.class).toInstance(sessionFactory);

        bind(UserDao.class);
        bind(SubsTeacherDao.class);
        bind(SubsConsultDao.class);
        bind(SubsCourseDao.class);
        bind(SubsGroupDao.class);
        bind(SubsPointsDao.class);
        bind(HashesDao.class);
        bind(ApiUserDao.class);
    }

    private void registerEntities(Configuration config) {
        config.addAnnotatedClass(SubsPoint.class);
        config.addAnnotatedClass(SubsTeacher.class);
        config.addAnnotatedClass(SubsConsult.class);
        config.addAnnotatedClass(SubsCourse.class);
        config.addAnnotatedClass(SubsGroup.class);
        config.addAnnotatedClass(ScheduleHash.class);
        config.addAnnotatedClass(BotUser.class);
        config.addAnnotatedClass(ApiUser.class);
        config.addAnnotatedClass(ApiSession.class);
    }

    private SessionFactory initHibernate() {
        return loadDrivers(() -> {
            Configuration configuration = new Configuration();

            configuration.addProperties(conf.getDbProperties());
            registerEntities(configuration);

            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties());

            return configuration.buildSessionFactory(builder.build());
        });
    }

    private <T> T loadDrivers(Supplier<T> factorySupplier) {
        Path driversDir = Paths.get("./drivers").toAbsolutePath();
        ClassLoader mainLoader = null;

        if (Files.exists(driversDir)) {
            LibLoader loader = new LibLoader();
            loader.loadAll(driversDir);
            loader.finishLoad();
            mainLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(loader.classLoader());
        }

        T result = factorySupplier.get();

        if (mainLoader != null)
            Thread.currentThread().setContextClassLoader(mainLoader);

        return result;
    }

}
