package edu.zieit.scheduler.webapi;

public final class ErrorKind {

    public static final String ACCESS_DENIED = "access_denied";
    public static final String INVALID_LOGIN = "invalid_login";
    public static final String INVALID_PASSWORD = "invalid_password";
    public static final String INVALID_TOKEN = "invalid_token";
    public static final String LOGIN_TAKEN = "login_taken";
    public static final String PASSWORD_SHORT = "password_short";
    public static final String USER_NOT_FOUND = "user_not_found";

    private ErrorKind() {}

}
