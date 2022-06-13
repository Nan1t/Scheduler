package edu.zieit.scheduler.server.controller;

import com.google.inject.Inject;
import edu.zieit.scheduler.persistence.entity.ApiSession;
import edu.zieit.scheduler.persistence.entity.ApiUser;
import edu.zieit.scheduler.server.entity.LoginRequest;
import edu.zieit.scheduler.server.entity.LoginResponse;
import edu.zieit.scheduler.server.entity.Response;
import edu.zieit.scheduler.server.entity.ResponseError;
import edu.zieit.scheduler.services.ApiUserService;
import edu.zieit.scheduler.util.BCrypt;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class AuthController {

    private static final Logger log = LogManager.getLogger();

    private final ApiUserService userService;

    @Inject
    public AuthController(ApiUserService userService) {
        this.userService = userService;
    }

    public void login(Context ctx) {
        LoginRequest request = ctx.bodyAsClass(LoginRequest.class);
        ApiUser user = userService.findUser(request.getLogin());

        if (user == null) {
            ctx.json(new ResponseError("invalid_login", ""));
            return;
        }

        String reqPassword = BCrypt.hashpw(request.getPassword(), user.getSalt());

        if (reqPassword.equals(user.getPassword())) {
            ApiSession session = new ApiSession();
            session.setUser(user);
            session.setAccessToken(UUID.randomUUID().toString());
            session.setAgent(ctx.userAgent());
            userService.saveSession(session);

            ctx.json(new LoginResponse(session.getAccessToken()));
            return;
        }

        ctx.json(new ResponseError("invalid_password", ""));
    }

    public void logout(Context ctx) {
        ApiSession session = ctx.attribute("session");
        userService.endSession(session);
        ctx.json(new Response(true));
    }

    public void authenticate(Context ctx) {
        String path = ctx.req.getPathInfo();

        if (path.equals("/login")) return;

        String accessToken = ctx.header("Authentication");

        if (accessToken == null) {
            throw new UnauthorizedResponse();
        }

        ApiSession session = userService.findSession(accessToken);

        if (session == null) {
            throw new UnauthorizedResponse();
        }

        ctx.attribute("session", session);

        log.info("User {} ({}) authorized with token {}",
                session.getUser().getLogin(),
                ctx.req.getRemoteAddr(),
                accessToken);
    }

}
