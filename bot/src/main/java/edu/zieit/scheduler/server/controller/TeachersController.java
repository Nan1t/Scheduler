package edu.zieit.scheduler.server.controller;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.config.ScheduleConfig;
import edu.zieit.scheduler.schedule.teacher.TeacherScheduleInfo;
import edu.zieit.scheduler.server.entity.Response;
import edu.zieit.scheduler.server.entity.TeacherProps;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TeachersController {

    private static final Logger log = LogManager.getLogger();

    private final ScheduleConfig config;

    @Inject
    public TeachersController(ScheduleConfig config) {
        this.config = config;
    }

    public void get(Context ctx) {
        TeacherProps props = new TeacherProps();
        Map<String, NamespacedKey> ass = config.getTeachers().getAssociations();
        Map<String, String> convertedAss = new HashMap<>();

        for (var entry : ass.entrySet()) {
            convertedAss.put(entry.getKey(), entry.getValue().toString());
        }

        props.setUrl(config.getTeachers().getUrl().toString());
        props.setAssociations(convertedAss);

        ctx.json(props);
    }

    public void update(Context ctx) {
        TeacherProps props = ctx.bodyAsClass(TeacherProps.class);
        URL url;
        Map<String, NamespacedKey> ass = new HashMap<>();

        try {
            url = new URL(props.getUrl());
        } catch (MalformedURLException e) {
            log.error("URL formatting error", e);
            throw new InternalServerErrorResponse();
        }

        for (var entry : props.getAssociations().entrySet()) {
            ass.put(entry.getKey(), NamespacedKey.parse(entry.getValue()));
        }

        config.setTeachers(new TeacherScheduleInfo(url, ass));

        try {
            config.save();
        } catch (IOException e) {
            log.error("Config saving error", e);
            throw new InternalServerErrorResponse();
        }

        ctx.json(new Response(true));
    }

}
