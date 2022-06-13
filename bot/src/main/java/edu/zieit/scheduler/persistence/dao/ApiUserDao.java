package edu.zieit.scheduler.persistence.dao;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.persistence.Dao;
import edu.zieit.scheduler.persistence.entity.ApiSession;
import edu.zieit.scheduler.persistence.entity.ApiUser;
import org.hibernate.SessionFactory;

public class ApiUserDao extends Dao {

    @Inject
    public ApiUserDao(SessionFactory factory) {
        super(factory);
    }

    public ApiUser getUser(String login) {
        return findValue(ApiUser.class, login);
    }

    public ApiSession getSession(String accessToken) {
        return findValue(ApiSession.class, accessToken);
    }

    public void save(ApiUser user) {
        withSession(session -> session.save(user));
    }

    public void saveSession(ApiSession s) {
        withSession(session -> session.save(s));
    }

    public void delete(ApiUser user) {
        withSession(session -> session.delete(user));
    }

    public void deleteSession(ApiSession s) {
        withSession(session -> session.delete(s));
    }
}
