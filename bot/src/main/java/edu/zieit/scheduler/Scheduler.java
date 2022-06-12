package edu.zieit.scheduler;

import com.google.inject.Guice;
import com.google.inject.Injector;
import edu.zieit.scheduler.api.Regexs;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.SchedulerBot;
import edu.zieit.scheduler.config.MainConfig;
import edu.zieit.scheduler.config.ScheduleConfig;
import edu.zieit.scheduler.inject.*;
import edu.zieit.scheduler.schedule.TimeTable;
import edu.zieit.scheduler.services.TimerService;
import edu.zieit.scheduler.webapi.WebServer;
import edu.zieit.scheduler.api.config.Language;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class Scheduler {

    private static final Logger logger = LogManager.getLogger(Scheduler.class);

    private SessionFactory sessionFactory;
    private SchedulerBot bot;
    private TimerService timer;
    private WebServer webServer;

    public void start() throws Exception {
        Path rootDir = Paths.get("./");
        MainConfig conf = new MainConfig(rootDir);
        ScheduleConfig scheduleConf = new ScheduleConfig(rootDir);

        conf.reload();
        scheduleConf.reload();

        Injector injector = Guice.createInjector(
                new BaseModule(rootDir, conf, scheduleConf),
                new PersistenceModule(conf),
                new ServicesModule(),
                new WebModule(),
                new BotModule()
        );

        Language lang = injector.getInstance(Language.class);

        lang.reload();

        Regexs.TEACHER = conf.getRegexTeacherDefault();
        Regexs.TEACHER_INLINE = conf.getRegexTeacherInline();
        Regexs.CLASSROOM = conf.getRegexClassroom();

        TimeTable.setDayIndexes(scheduleConf.getDayIndexes());

        sessionFactory = injector.getInstance(SessionFactory.class);

        ScheduleService scheduleService = injector.getInstance(ScheduleService.class);

        logger.info("Loading schedule ...");
        scheduleService.reloadAll();
        logger.info("All schedule loaded");

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        bot = injector.getInstance(SchedulerBot.class);

        logger.info("Starting long polling bot ...");
        botsApi.registerBot(bot);

        timer = injector.getInstance(TimerService.class);
        timer.start();
        logger.info("Started timer");

        webServer = injector.getInstance(WebServer.class);
        webServer.start(conf);

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "Scheduler shutdown thread"));

        logger.info("Done! Scheduler ready to work");
    }

    public void shutdown() {
        webServer.stop();
        logger.info("Stopping timer ...");
        timer.stop();
        logger.info("Closing connections ...");
        sessionFactory.close();
        logger.info("Closing long polling bot ...");
        bot.shutdown();
        System.out.println("Goodbye!");
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
