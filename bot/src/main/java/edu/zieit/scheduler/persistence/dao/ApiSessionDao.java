package edu.zieit.scheduler.persistence.dao;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.persistence.Dao;
import edu.zieit.scheduler.persistence.webapi.ApiSession;
import org.hibernate.SessionFactory;

import java.util.List;

public class ApiSessionDao extends Dao {

    @Inject
    public ApiSessionDao(SessionFactory factory) {
        super(factory);
    }

    public List<ApiSession> listAll() {
        return useSession(session ->
                session.createQuery("from ApiSession", ApiSession.class)
                        .getResultList());
    }

    public ApiSession find(String accessToken) {
        return findValue(ApiSession.class, accessToken);
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
