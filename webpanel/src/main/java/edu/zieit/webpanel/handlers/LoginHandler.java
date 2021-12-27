package edu.zieit.webpanel.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class LoginHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        String login = ctx.queryParam("login");
        String password = ctx.queryParam("password");

        ctx.result(String.format("Login: %s, Password: %s", login, password));
    }

}
