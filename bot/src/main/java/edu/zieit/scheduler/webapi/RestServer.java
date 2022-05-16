package edu.zieit.scheduler.webapi;

import edu.zieit.scheduler.config.MainConfig;
import io.javalin.Javalin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RestServer {

    private static final Logger logger = LogManager.getLogger(RestServer.class);

    private Javalin api;

    public void start(MainConfig conf) {
        if (conf.isEnableRest()) {
            api = Javalin.create().start(conf.getRestApiPort());


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
