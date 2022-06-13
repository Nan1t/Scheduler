package edu.zieit.scheduler.server.controller;

import com.google.inject.Inject;
import edu.zieit.scheduler.config.ScheduleConfig;
import edu.zieit.scheduler.server.entity.BasicProperties;
import edu.zieit.scheduler.server.entity.Response;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class PropertiesController {

    private static final Logger log = LogManager.getLogger();

    private final ScheduleConfig config;

    @Inject
    public PropertiesController(ScheduleConfig config) {
        this.config = config;
    }

    public void get(Context ctx) {
        BasicProperties props = new BasicProperties();
        props.setCheckRate(config.getCheckRate());
        props.setCompAuds(config.getCompAud());
        props.setDayIndexes(config.getDayIndexes());
        ctx.json(props);
    }

    public void update(Context ctx) {
        BasicProperties props = ctx.bodyAsClass(BasicProperties.class);

        config.setCheckRate(props.getCheckRate());
        config.setCompAud(props.getCompAuds());
        config.setDayIndexes(props.getDayIndexes());

        try {
            config.save();
        } catch (IOException e) {
            log.error("Config saving error", e);
            throw new InternalServerErrorResponse();
        }

        ctx.json(new Response(true));
    }

}
