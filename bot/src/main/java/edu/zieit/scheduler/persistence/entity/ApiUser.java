package edu.zieit.scheduler.persistence.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "api_users")
public class ApiUser {

    @Id
    private String login;
    private String password;
    private String salt;
    private boolean admin;

    @OneToMany(mappedBy = "user")
    private List<ApiSession> sessions;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public List<ApiSession> getSessions() {
        return sessions;
    }

    public void setSessions(List<ApiSession> sessions) {
        this.sessions = sessions;
    }
}
