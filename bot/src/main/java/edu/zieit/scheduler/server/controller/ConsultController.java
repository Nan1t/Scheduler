package edu.zieit.scheduler.server.controller;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.config.ScheduleConfig;
import edu.zieit.scheduler.schedule.consult.ConsultScheduleInfo;
import edu.zieit.scheduler.server.entity.ConsultProps;
import edu.zieit.scheduler.server.entity.Response;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ConsultController {

    private static final Logger log = LogManager.getLogger(ConsultController.class);

    private final ScheduleConfig config;
    private final ScheduleService service;

    @Inject
    public ConsultController(ScheduleConfig config, ScheduleService service) {
        this.config = config;
        this.service = service;
    }

    public void get(Context ctx) {
        ConsultProps props = new ConsultProps();
        ConsultProps.Point dayPoint = new ConsultProps.Point();
        ConsultProps.Point teacherPoint = new ConsultProps.Point();

        dayPoint.setCol(config.getConsult().dayPoint().col());
        dayPoint.setRow(config.getConsult().dayPoint().row());

        teacherPoint.setCol(config.getConsult().teacherPoint().col());
        teacherPoint.setRow(config.getConsult().teacherPoint().row());

        props.setUrl(config.getConsult().getUrl().toString());
        props.setDayPoint(dayPoint);
        props.setTeacherPoint(teacherPoint);

        ctx.json(props);
    }

    public void update(Context ctx) {
        ConsultProps props = ctx.bodyAsClass(ConsultProps.class);
        URL url;

        try {
            url = new URL(props.getUrl());
        } catch (MalformedURLException e) {
            log.error("URL formatting error", e);
            throw new InternalServerErrorResponse();
        }

        config.setConsult(new ConsultScheduleInfo(
                url,
                new SheetPoint(props.getDayPoint().getCol(), props.getDayPoint().getRow()),
                new SheetPoint(props.getTeacherPoint().getCol(), props.getTeacherPoint().getRow())
        ));

        try {
            config.save();
        } catch (IOException e) {
            log.error("Config saving error", e);
            throw new InternalServerErrorResponse();
        }

        service.reloadAll();

        ctx.json(new Response(true));
    }

}
