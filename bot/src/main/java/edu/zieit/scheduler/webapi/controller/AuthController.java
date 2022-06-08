package edu.zieit.scheduler.webapi.controller;

import com.google.inject.Inject;
import edu.zieit.scheduler.webapi.entity.Response;
import io.javalin.http.Context;

public class AuthController {

//    private final ApiUserService userService;
//
//    @Inject
//    public AuthController(ApiUserService userService) {
//        this.userService = userService;
//    }

    public void login(Context ctx) {
        ctx.json(new Response(true));
    }

    public void logout(Context ctx) {
        ctx.json(new Response(true));
    }

}
