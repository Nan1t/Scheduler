package edu.zieit.webpanel;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.http.staticfiles.Location;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class WebPanel {

    private Javalin app;

    public void start() {
        app = Javalin.create(this::configure).start(8080);

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver(getClass().getClassLoader());
        TemplateEngine engine = new TemplateEngine();

        resolver.setPrefix("");
        resolver.setSuffix(".html");
        engine.setTemplateResolver(resolver);

        app.get("/", ctx -> {
            Context context = new Context();
            String html = engine.process("index", context);
            ctx.html(html);
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
        new WebPanel().start();
    }

}
