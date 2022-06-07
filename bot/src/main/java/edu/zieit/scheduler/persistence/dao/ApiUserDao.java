package edu.zieit.scheduler.persistence.dao;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.persistence.Dao;
import org.hibernate.SessionFactory;

import java.util.List;

public class ApiUserDao extends Dao {

    @Inject
    public ApiUserDao(SessionFactory factory) {
        super(factory);
    }

    public List<ApiUser> listAll() {
        return useSession(session ->
                session.createQuery("from ApiUser", ApiUser.class)
                        .getResultList());
    }

    public ApiUser find(String login) {
        return findValue(ApiUser.class, login);
    }

    public void create(ApiUser user) {
        withSession(session -> session.save(user));
    }

    public void update(ApiUser user) {
        withSession(session -> session.update(user));
    }

    public void delete(ApiUser user) {
        withSession(session -> session.delete(user));
    }
}
