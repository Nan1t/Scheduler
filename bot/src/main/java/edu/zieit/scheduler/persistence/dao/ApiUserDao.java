package edu.zieit.scheduler.persistence.dao;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.persistence.Dao;
import edu.zieit.scheduler.persistence.entity.ApiSession;
import edu.zieit.scheduler.persistence.entity.ApiUser;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class ApiUserDao extends Dao {

    @Inject
    public ApiUserDao(SessionFactory factory) {
        super(factory);
    }

    public ApiUser getUser(String login) {
        return findValue(ApiUser.class, login);
    }

    public List<ApiUser> getAllUsers() {
        return useSession(session -> {
            Query<?> query = session.createQuery("select * from ApiUser");
            return (List<ApiUser>) query.list();
        });
    }

    public long countUsers() {
        return useSession(session -> {
            Query<?> query = session.createQuery("select count(*) from ApiUser");
            return (Long) query.uniqueResult();
        });
    }

    public ApiSession getSession(String accessToken) {
        return findValue(ApiSession.class, accessToken);
    }

    public List<ApiSession> getAllSessions() {
        return useSession(session -> {
            Query<?> query = session.createQuery("select * from ApiSession");
            return (List<ApiSession>) query.list();
        });
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
