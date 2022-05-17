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

            api.get("/login", new StatsHandler());
            api.post("/logout", new StatsHandler());
            api.get("/stats", new StatsHandler());
            api.get("/checkRate/get", new StatsHandler());
            api.post("/checkRate/edit", new StatsHandler());
            api.get("/computerRooms/get", new StatsHandler());
            api.post("/computerRooms/edit", new StatsHandler());
            api.get("/dayIndexes/get", new StatsHandler());
            api.post("/dayIndexes/edit", new StatsHandler());
            api.get("/teachers/get", new StatsHandler());
            api.post("/teachers/edit", new StatsHandler());
            api.get("/consult/get", new StatsHandler());
            api.post("/consult/edit", new StatsHandler());
            api.get("/courses/get", new StatsHandler());
            api.post("/courses/edit", new StatsHandler());
            api.get("/rendering/get", new StatsHandler());
            api.post("/rendering/edit", new StatsHandler());

            api.get("/user/list", new StatsHandler());
            api.get("/user/sessions", new StatsHandler());
            api.post("/user/endSession", new StatsHandler());
            api.post("/user/create", new StatsHandler());
            api.post("/user/edit", new StatsHandler());
            api.post("/user/delete", new StatsHandler());

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
