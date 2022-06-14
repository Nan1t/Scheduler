package edu.zieit.scheduler.server.controller;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.render.DocRenderOptions;
import edu.zieit.scheduler.config.ScheduleConfig;
import edu.zieit.scheduler.server.entity.RenderingProps;
import edu.zieit.scheduler.server.entity.Response;
import io.javalin.http.Context;

public class RenderingController {

    private final ScheduleConfig config;

    @Inject
    public RenderingController(ScheduleConfig config) {
        this.config = config;
    }

    public void get(Context ctx) {
        RenderingProps props = new RenderingProps();
        props.setFormat(config.getRenderOptions().format().toString());
        props.setDpi(config.getRenderOptions().dpi());
        ctx.json(props);
    }

    public void update(Context ctx) {
        RenderingProps props = ctx.bodyAsClass(RenderingProps.class);

        config.setRenderOptions(new DocRenderOptions(
                DocRenderOptions.Format.valueOf(props.getFormat()),
                props.getDpi()
        ));

        ctx.json(new Response(true));
    }

}
