package edu.zieit.scheduler.server.entity.user;

public class SessionEntity {

    private String login;
    private String token;
    private String agent;

    public SessionEntity(String login, String token, String agent) {
        this.login = login;
        this.token = token;
        this.agent = agent;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }
}
