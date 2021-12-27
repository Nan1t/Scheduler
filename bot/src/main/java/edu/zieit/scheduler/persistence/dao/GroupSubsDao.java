package edu.zieit.scheduler.persistence.dao;

import edu.zieit.scheduler.api.persistence.Dao;
import edu.zieit.scheduler.persistence.subscription.SubscriptionGroup;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Collection;

public class GroupSubsDao extends Dao {

    public GroupSubsDao(SessionFactory factory) {
        super(factory);
    }

    public SubscriptionGroup find(String tgId) {
        return findValue(SubscriptionGroup.class, tgId);
    }

    public Collection<SubscriptionGroup> findNotMailed(int limit) {
        return useSession(session -> {
            Query<?> q = session.createQuery("from SubscriptionGroup where received_mailing = false");
            q.setMaxResults(limit);
            return (Collection<SubscriptionGroup>) q.list();
        });
    }

    public void save(SubscriptionGroup sub) {
        withSession(session -> session.saveOrUpdate(sub));
    }

    public void update(Collection<SubscriptionGroup> subs) {
        withSession(session -> {
            int i = 0;
            for (SubscriptionGroup sub : subs) {
                session.update(sub);

                if (i % 50 == 0) {
                    session.flush();
                    session.clear();
                }

                i++;
            }
        });
    }

    public void resetMailing(Collection<String> groups) {
        withSession(session -> session.createQuery("update SubscriptionGroup " +
                "set received_mailing = false where group_name in (:groups)")
                .setParameterList("groups", groups)
                .executeUpdate());
    }

    public boolean delete(String tgId) {
        int res = execUpdate("delete from SubscriptionGroup where tg_id = :tg_id",
                q -> q.setParameter("tg_id", tgId));
        return res > 0;
    }

}
