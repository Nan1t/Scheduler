package edu.zieit.scheduler.webapi;

import com.google.inject.Inject;
import edu.zieit.scheduler.persistence.webapi.ApiSession;
import edu.zieit.scheduler.services.ApiUserService;
import edu.zieit.scheduler.util.TimeUtil;
import io.javalin.core.security.AccessManager;
import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class AuthHandler implements AccessManager {

    private final ApiUserService userService;

    @Inject
    public AuthHandler(ApiUserService userService) {
        this.userService = userService;
    }

    @Override
    public void manage(@NotNull Handler handler, @NotNull Context ctx, @NotNull Set<RouteRole> routeRoles) {
        if (ctx.endpointHandlerPath().equals("/login")) return;

        String accessToken = ctx.header("Authorization");

        if (accessToken != null) {
            ApiSession session = userService.getSession(accessToken);

            if (session != null) {
                if (session.getExpiryAfter() > TimeUtil.currentUnixTime()) {
                    ctx.attribute("session", session);
                    System.out.println("Confirmed session " + session);
                    return;
                }

                userService.deleteSession(session);
                System.out.println("Delete session " + session.getAccessToken());
            }
        }

        ctx.status(401).json("Unauthorized");
    }
}
