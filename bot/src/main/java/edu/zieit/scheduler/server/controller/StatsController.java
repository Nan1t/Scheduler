package edu.zieit.scheduler.server.controller;

import com.google.inject.Inject;
import edu.zieit.scheduler.server.entity.StatsResponse;
import edu.zieit.scheduler.services.SubsService;
import io.javalin.http.Context;

public class StatsController {

    private final SubsService subsService;

    @Inject
    public StatsController(SubsService subsService) {
        this.subsService = subsService;
    }

    public void get(Context ctx) {
        ctx.json(new StatsResponse(
                subsService.countUsers(),
                subsService.countTeacherSubs(),
                subsService.countConsultSubs(),
                subsService.countCourseSubs(),
                subsService.countGroupSubs(),
                subsService.countPointsSubs()
        ));
    }

}
