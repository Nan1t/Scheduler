package edu.zieit.scheduler.server.controller;

import com.google.inject.Inject;
import edu.zieit.scheduler.persistence.entity.ApiSession;
import edu.zieit.scheduler.persistence.entity.ApiUser;
import edu.zieit.scheduler.server.entity.Response;
import edu.zieit.scheduler.server.entity.ResponseError;
import edu.zieit.scheduler.server.entity.user.*;
import edu.zieit.scheduler.services.ApiUserService;
import edu.zieit.scheduler.util.BCrypt;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;

import java.util.List;

public class UserController {

    private final ApiUserService service;

    @Inject
    public UserController(ApiUserService service) {
        this.service = service;
    }

    public void listUsers(Context ctx) {
        validateAccess(ctx);

        List<ApiUser> users = service.getAllUsers();
        List<UserEntity> entities = users.stream()
                .map(e -> new UserEntity(e.getLogin(), null, e.isAdmin()))
                .toList();
        UsersResponse response = new UsersResponse();
        response.setUsers(entities);
        ctx.json(response);
    }

    public void listSessions(Context ctx) {
        validateAccess(ctx);

        List<ApiSession> users = service.getAllSessions();
        List<SessionEntity> entities = users.stream()
                .map(e -> new SessionEntity(e.getUser().getLogin(), e.getAccessToken(), e.getAgent()))
                .toList();
        SessionsResponse response = new SessionsResponse();
        response.setSessions(entities);
        ctx.json(response);
    }

    public void endSession(Context ctx) {
        validateAccess(ctx);

        EndSessionRequest req = ctx.bodyAsClass(EndSessionRequest.class);
        ApiSession session = service.findSession(req.getToken());

        if (session == null) {
            ctx.json(new ResponseError("invalid_token", ""));
            return;
        }

        service.endSession(session);
        ctx.json(new Response(true));
    }

    public void createUser(Context ctx) {
        validateAccess(ctx);

        UserEntity req = ctx.bodyAsClass(UserEntity.class);

        if (req.getPassword().length() < 4) {
            ctx.json(new ResponseError("password_short", ""));
            return;
        }

        ApiUser user = service.findUser(req.getLogin());

        if (user != null) {
            ctx.json(new ResponseError("login_taken", ""));
            return;
        }

        service.createUser(req.getLogin(), req.getPassword(), req.isAdmin());
        ctx.json(new Response(true));
    }

    public void editUser(Context ctx) {
        validateAccess(ctx);

        UserEntity req = ctx.bodyAsClass(UserEntity.class);
        ApiUser user = service.findUser(req.getLogin());

        if (user == null) {
            ctx.json(new ResponseError("user_not_found", ""));
            return;
        }

        if (req.getPassword() != null) {
            if (req.getPassword().length() < 4) {
                ctx.json(new ResponseError("password_short", ""));
                return;
            }

            String salt = BCrypt.gensalt();
            String passwHash = BCrypt.hashpw(req.getPassword(), salt);
            user.setPassword(passwHash);
        }

        user.setAdmin(req.isAdmin());
        user.setSessions(null);
        service.saveUser(user);

        ctx.json(new Response(true));
    }

    public void deleteUser(Context ctx) {
        validateAccess(ctx);

        DeleteUserRequest req = ctx.bodyAsClass(DeleteUserRequest.class);
        ApiUser user = service.findUser(req.getLogin());

        if (user == null) {
            ctx.json(new ResponseError("user_not_found", ""));
            return;
        }

        service.deleteUser(user);
        ctx.json(new Response(true));
    }

    private void validateAccess(Context ctx) {
        ApiSession session = ctx.attribute("session");
        if (!session.getUser().isAdmin()) {
            throw new UnauthorizedResponse();
        }
    }

}
