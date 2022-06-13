package edu.zieit.scheduler.server;

import com.google.inject.Inject;
import edu.zieit.scheduler.config.MainConfig;
import edu.zieit.scheduler.server.controller.*;
import io.javalin.Javalin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebServer {

    private static final Logger logger = LogManager.getLogger(WebServer.class);

    private Javalin api;

    private final AuthController authController;
    private final StatsController statsController;
    private final PropertiesController propsController;
    private final TeachersController teachersController;
    private final ConsultController consultController;
    private final CoursesController coursesController;
    private final RenderingController renderingController;
    private final UserController userController;

    @Inject
    public WebServer(
            AuthController authController,
            StatsController statsController,
            PropertiesController propsController,
            TeachersController teachersController,
            ConsultController consultController,
            CoursesController coursesController,
            RenderingController renderingController,
            UserController userController
    ) {
        this.authController = authController;
        this.statsController = statsController;
        this.propsController = propsController;
        this.teachersController = teachersController;
        this.consultController = consultController;
        this.coursesController = coursesController;
        this.renderingController = renderingController;
        this.userController = userController;
    }

    public void start(MainConfig conf) {
        if (conf.isEnableRest()) {
            api = Javalin.create(
                    config -> config.jsonMapper(new GsonMapper())
            ).start(conf.getRestApiPort());

            api.before(authController::authenticate);

            api.post("/login", authController::login);
            api.get("/logout", authController::logout);
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
