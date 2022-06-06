package edu.zieit.scheduler.inject;

import com.google.inject.AbstractModule;
import edu.zieit.scheduler.webapi.AuthHandler;
import edu.zieit.scheduler.webapi.WebServer;
import edu.zieit.scheduler.webapi.controller.*;

public class WebModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AuthHandler.class);
        bind(AuthController.class);
        bind(StatsController.class);
        bind(PropertiesController.class);
        bind(TeachersController.class);
        bind(ConsultController.class);
        bind(CoursesController.class);
        bind(RenderingController.class);
        bind(UserController.class);
        bind(WebServer.class);
    }

}
