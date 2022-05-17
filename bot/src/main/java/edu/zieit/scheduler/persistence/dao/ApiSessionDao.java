package edu.zieit.scheduler.persistence.dao;

import edu.zieit.scheduler.api.persistence.Dao;
import edu.zieit.scheduler.persistence.webapi.ApiSession;
import edu.zieit.scheduler.persistence.webapi.ApiUser;
import org.hibernate.SessionFactory;

import java.util.List;

public class ApiSessionDao extends Dao {

    public ApiSessionDao(SessionFactory factory) {
        super(factory);
    }

    public List<ApiSession> listAll() {
        return useSession(session ->
                session.createQuery("from ApiSession", ApiSession.class)
                        .getResultList());
    }

    public ApiSession find(String login) {
        return findValue(ApiSession.class, login);
    }

    public void create(ApiSession user) {
        withSession(session -> session.save(user));
    }

    public void update(ApiSession user) {
        withSession(session -> session.update(user));
    }

    public void delete(ApiSession user) {
        withSession(session -> session.delete(user));
    }
}
