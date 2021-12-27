package edu.zieit.scheduler.persistence.dao;

import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.api.persistence.Dao;
import edu.zieit.scheduler.persistence.subscription.SubscriptionCourse;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Collection;

public class CourseSubsDao extends Dao {

    public CourseSubsDao(SessionFactory factory) {
        super(factory);
    }

    public SubscriptionCourse find(String tgId) {
        return findValue(SubscriptionCourse.class, tgId);
    }

    public Collection<SubscriptionCourse> findNotMailed(int limit) {
        return useSession(session -> {
            Query<?> q = session.createQuery("from SubscriptionCourse where received_mailing = false");
            q.setMaxResults(limit);
            return (Collection<SubscriptionCourse>) q.list();
        });
    }

    public void save(SubscriptionCourse sub) {
        withSession(session -> session.saveOrUpdate(sub));
    }

    public void update(Collection<SubscriptionCourse> subs) {
        withSession(session -> {
            int i = 0;
            for (SubscriptionCourse sub : subs) {
                session.update(sub);

                if (i % 50 == 0) {
                    session.flush();
                    session.clear();
                }

                i++;
            }
        });
    }

    public void resetMailing(Collection<NamespacedKey> keys) {
        withSession(session -> session.createQuery("update SubscriptionCourse " +
                "set received_mailing = false " +
                "where schedule_key in (:keys)")
                .setParameterList("keys", keys)
                .executeUpdate());
    }

    public boolean delete(String tgId) {
        int res = execUpdate("delete from SubscriptionCourse where tg_id = :tg_id",
                q -> q.setParameter("tg_id", tgId));
        return res > 0;
    }

}
