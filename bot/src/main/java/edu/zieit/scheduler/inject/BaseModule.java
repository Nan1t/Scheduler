package edu.zieit.scheduler.inject;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import edu.zieit.scheduler.config.MainConfig;
import edu.zieit.scheduler.config.ScheduleConfig;
import edu.zieit.scheduler.api.config.Language;

import java.nio.file.Path;

public class BaseModule extends AbstractModule {

    private final Path rootDir;
    private final MainConfig conf;
    private final ScheduleConfig scheduleConf;

    public BaseModule(Path rootDir, MainConfig conf, ScheduleConfig scheduleConf) {
        this.rootDir = rootDir;
        this.conf = conf;
        this.scheduleConf = scheduleConf;
    }

    @Override
    protected void configure() {
        bind(Path.class)
                .annotatedWith(Names.named("appDir"))
                .toInstance(rootDir);

        bind(MainConfig.class).toInstance(conf);
        bind(ScheduleConfig.class).toInstance(scheduleConf);
        bind(Language.class).toInstance(new Language(rootDir, "/lang.yml"));
    }

}
