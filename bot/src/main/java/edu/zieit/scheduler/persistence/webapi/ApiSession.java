package edu.zieit.scheduler.persistence.webapi;

import javax.persistence.*;

@Entity
@Table(name = "api_sessions")
public class ApiSession {

    @Id
    @Column(name = "access_token")
    private String accessToken;

//    @Column(name = "user_login")
//    private String userLogin;

    @Column(name = "expiry_after")
    private long expiryAfter;
    private String agent;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_login")
    private ApiUser user;

    public ApiSession() {}

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

//    public String getUserLogin() {
//        return userLogin;
//    }
//
//    public void setUserLogin(String userLogin) {
//        this.userLogin = userLogin;
//    }

    public long getExpiryAfter() {
        return expiryAfter;
    }

    public void setExpiryAfter(long expiryAfter) {
        this.expiryAfter = expiryAfter;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public ApiUser getUser() {
        return user;
    }

    public void setUser(ApiUser user) {
        this.user = user;
    }
}
