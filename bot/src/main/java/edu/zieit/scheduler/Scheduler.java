package edu.zieit.scheduler;

import edu.zieit.scheduler.config.MainConfig;
import edu.zieit.scheduler.config.ScheduleConfig;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Scheduler {

    private MainConfig conf;
    private ScheduleConfig scheduleConf;

    public void start() throws Exception {
        Path rootDir = Paths.get("./");

        conf = new MainConfig(rootDir);
        scheduleConf = new ScheduleConfig(rootDir);

        conf.reload();
        scheduleConf.reload();

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "Safe shutdown thread"));
    }

    public void shutdown() {

    }

}
