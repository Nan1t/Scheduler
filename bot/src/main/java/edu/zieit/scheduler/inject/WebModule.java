package edu.zieit.scheduler.inject;

import com.google.inject.AbstractModule;
import edu.zieit.scheduler.server.WebServer;
import edu.zieit.scheduler.server.controller.*;

public class WebModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AuthController.class);
        bind(StatsController.class);
        bind(PropertiesController.class);
        bind(TeachersController.class);
        bind(ConsultController.class);
        bind(CoursesController.class);
        bind(UserController.class);
        bind(WebServer.class);
    }

}
