package edu.zieit.scheduler.webapi;

import edu.zieit.scheduler.config.MainConfig;
import edu.zieit.scheduler.webapi.controller.*;
import io.javalin.Javalin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebServer {

    private static final Logger logger = LogManager.getLogger(WebServer.class);

    private Javalin api;

    public void start(MainConfig conf) {
        if (conf.isEnableRest()) {
            api = Javalin.create(/*config -> config.accessManager(new AuthHandler())*/)
                    .start(conf.getRestApiPort());

            var authController = new AuthController();
            var statsController = new StatsController();
            var propsController = new PropertiesController();
            var teachersController = new TeachersController();
            var consultController = new ConsultController();
            var coursesController = new CoursesController();
            var renderingController = new RenderingController();
            var userController = new UserController();

            api.get("/login", authController::login);
            api.post("/logout", authController::logout);
            api.get("/stats", statsController::get);
            api.get("/properties", propsController::get);
            api.post("/properties", propsController::update);
            api.get("/teachers", teachersController::get);
            api.post("/teachers", teachersController::update);
            api.get("/consult", consultController::get);
            api.post("/consult", consultController::update);
            api.get("/courses", coursesController::get);
            api.post("/courses", coursesController::update);
            api.get("/rendering", renderingController::get);
            api.post("/rendering", renderingController::update);

            api.get("/user/list", userController::listUsers);
            api.get("/user/sessions", userController::listSessions);
            api.post("/user/endSession", userController::endSession);
            api.post("/user/create", userController::createUser);
            api.post("/user/edit", userController::editUser);
            api.post("/user/delete", userController::deleteUser);

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
