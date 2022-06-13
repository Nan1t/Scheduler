package edu.zieit.scheduler.persistence.dao;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.persistence.Dao;
import edu.zieit.scheduler.persistence.entity.SubsGroup;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Collection;

public class SubsGroupDao extends Dao {

    @Inject
    public SubsGroupDao(SessionFactory factory) {
        super(factory);
    }

    public SubsGroup find(String tgId) {
        return findValue(SubsGroup.class, tgId);
    }

    public Collection<SubsGroup> findNotNotified(int limit) {
        return useSession(session -> {
            Query<?> q = session.createQuery("from SubsGroup where notified = false");
            q.setMaxResults(limit);
            return (Collection<SubsGroup>) q.list();
        });
    }

    public void save(SubsGroup sub) {
        withSession(session -> session.save(sub));
    }

    public void update(Collection<SubsGroup> subs) {
        withSession(session -> {
            int i = 0;
            for (SubsGroup sub : subs) {
                session.update(sub);

                if (i % 50 == 0) {
                    session.flush();
                    session.clear();
                }

                i++;
            }
        });
    }

    public void resetNotifications(Collection<String> groups) {
        withSession(session -> session.createQuery("update SubsGroup " +
                "set notified = false where group in (:groups)")
                .setParameterList("groups", groups)
                .executeUpdate());
    }

    public boolean delete(String tgId) {
        int res = execUpdate("delete from SubsGroup where tg_id = :tg_id",
                q -> q.setParameter("tg_id", tgId));
        return res > 0;
    }

    public long count() {
        return useSession(session -> {
            Query<?> query = session.createQuery("select count(*) from SubsGroup");
            return (Long) query.uniqueResult();
        });
    }

}
