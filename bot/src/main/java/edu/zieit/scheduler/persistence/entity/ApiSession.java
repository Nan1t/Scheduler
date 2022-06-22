package edu.zieit.scheduler.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "api_sessions")
public class ApiSession {

    @Id
    @Column(name = "access_token")
    private String accessToken;
    private String agent;

    @ManyToOne
    @JoinColumn(name="user_login", nullable=false)
    private ApiUser user;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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
