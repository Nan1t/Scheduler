package edu.zieit.scheduler.server.controller;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.SheetPoint;
import edu.zieit.scheduler.config.ScheduleConfig;
import edu.zieit.scheduler.schedule.course.CourseScheduleInfo;
import edu.zieit.scheduler.server.entity.CoursesProps;
import edu.zieit.scheduler.server.entity.Response;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class CoursesController {

    private static final Logger log = LogManager.getLogger(CoursesController.class);

    private final ScheduleConfig config;

    @Inject
    public CoursesController(ScheduleConfig config) {
        this.config = config;
    }

    public void get(Context ctx) {
        List<CoursesProps.Course> courses = new LinkedList<>();

        for (var el : config.getCourses()) {
            CoursesProps.Course course = new CoursesProps.Course();

            course.setUrl(el.getUrl().toString());
            course.setName(el.getDisplayName());

            CoursesProps.Point dayPoint = new CoursesProps.Point();
            CoursesProps.Point groupPoint = new CoursesProps.Point();

            dayPoint.setCol(el.getDayPoint().col());
            dayPoint.setRow(el.getDayPoint().row());

            groupPoint.setCol(el.getGroupPoint().col());
            groupPoint.setRow(el.getGroupPoint().row());

            course.setDayPoint(dayPoint);
            course.setGroupPoint(groupPoint);

            courses.add(course);
        }

        CoursesProps props = new CoursesProps();
        props.setCourses(courses);

        ctx.json(props);
    }

    public void update(Context ctx) {
        CoursesProps props = ctx.bodyAsClass(CoursesProps.class);
        List<CourseScheduleInfo> courses = new LinkedList<>();

        for (var el : props.getCourses()) {
            URL url;

            try {
                url = new URL(el.getUrl());
            } catch (MalformedURLException e) {
                log.error("URL formatting error", e);
                throw new InternalServerErrorResponse();
            }

            SheetPoint dayPoint = new SheetPoint(el.getDayPoint().getCol(), el.getDayPoint().getRow());
            SheetPoint groupPoint = new SheetPoint(el.getGroupPoint().getCol(), el.getGroupPoint().getRow());

            courses.add(new CourseScheduleInfo(url, el.getName(), dayPoint, groupPoint));
        }

        config.setCourses(courses);

        try {
            config.save();
        } catch (IOException e) {
            log.error("Config saving error", e);
            throw new InternalServerErrorResponse();
        }

        ctx.json(new Response(true));
    }

}
