package edu.zieit.webpanel;

import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.webpanel.handlers.LoginHandler;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.http.staticfiles.Location;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.File;

public class WebPanel {

    private final ScheduleService scheduleService;

    private Javalin app;
    private TemplateEngine engine;

    public WebPanel(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    public void start() {
        app = Javalin.create(this::configure).start(8080);

        engine = new TemplateEngine();
        AbstractConfigurableTemplateResolver resolver;

        if (System.getenv("DEVPATH") != null) {
            String path = System.getenv("DEVPATH");
            resolver = new FileTemplateResolver();
            resolver.setPrefix(path + File.separator);
            engine.setCacheManager(DevCacheManager.INSTANCE);
        } else {
            resolver = new ClassLoaderTemplateResolver(getClass().getClassLoader());
            resolver.setPrefix("");
        }

        resolver.setSuffix(".html");
        engine.setTemplateResolver(resolver);

        route("/", "index", new Context());
        route("/login", "login", new Context());
        route("/schedule", "schedule", new Context());

        app.get("/api/login", new LoginHandler());
    }

    private void route(String path, String tmpl, Context ctx) {
        app.get(path, c -> {
            String html = engine.process(tmpl, ctx);
            c.html(html);
        });
    }

    public void stop() {
        if (app != null)
            app.stop();
    }

    private void configure(JavalinConfig conf) {
        conf.addStaticFiles("/_static", Location.CLASSPATH);
    }

    public static void main(String[] args) {
        new WebPanel(null).start();
    }

}
