package edu.zieit.scheduler.persistence.dao;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.persistence.Dao;
import edu.zieit.scheduler.persistence.entity.BotUser;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.annotation.Nullable;

public class UserDao extends Dao {

    @Inject
    public UserDao(SessionFactory factory) {
        super(factory);
    }

    @Nullable
    public BotUser find(String tgId) {
        return findValue(BotUser.class, tgId);
    }

    public void save(BotUser user) {
        withSession(session -> session.saveOrUpdate(user));
    }

    public long count() {
        return useSession(session -> {
            Query<?> query = session.createQuery("select count(*) from BotUser");
            return (Long) query.uniqueResult();
        });
    }

}
