package edu.zieit.scheduler.persistence.dao;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.persistence.Dao;
import edu.zieit.scheduler.persistence.entity.BotUser;
import edu.zieit.scheduler.persistence.entity.SubsTeacher;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.annotation.Nullable;
import java.util.Collection;

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

    public Collection<BotUser> findNotNotified(int limit) {
        return useSession(session -> {
            Query<?> q = session.createQuery("from BotUser where notifications = true and notified = false");
            q.setMaxResults(limit);
            return (Collection<BotUser>) q.list();
        });
    }

    public void resetNotifications() {
        withSession(session -> session.createQuery("update BotUser set notified = false where notifications = true")
                .executeUpdate());
    }

    public long count() {
        return useSession(session -> {
            Query<?> query = session.createQuery("select count(*) from BotUser");
            return (Long) query.uniqueResult();
        });
    }

}
