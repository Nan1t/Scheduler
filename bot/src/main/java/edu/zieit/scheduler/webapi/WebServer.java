package edu.zieit.scheduler.webapi;

import edu.zieit.scheduler.config.MainConfig;
import edu.zieit.scheduler.webapi.handlers.StatsHandler;
import io.javalin.Javalin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebServer {

    private static final Logger logger = LogManager.getLogger(WebServer.class);

    private Javalin api;

    public void start(MainConfig conf) {
        if (conf.isEnableRest()) {
            api = Javalin.create().start(conf.getRestApiPort());

            api.get("/stats", new StatsHandler());

            logger.info("WEB server starter");
        }
    }

    public void stop() {
        if (api != null) {
            logger.info("Stopping WEB server ...");
            api.stop();
            logger.info("WEB server stopped");
        }
    }

}
