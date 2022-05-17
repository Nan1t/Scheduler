package edu.zieit.scheduler.persistence.webapi;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "api_users")
public class ApiUser {

    @Id
    private String login;
    private String password;
    private String salt;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @OneToMany(mappedBy = "user")
    private List<ApiSession> sessions;

    public ApiUser() {}

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
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public List<ApiSession> getSessions() {
        return sessions;
    }

    public void setSessions(List<ApiSession> sessions) {
        this.sessions = sessions;
    }
}
