package edu.zieit.scheduler.persistence.dao;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.persistence.Dao;
import edu.zieit.scheduler.persistence.entity.SubsCourse;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Collection;

public class SubsCourseDao extends Dao {

    @Inject
    public SubsCourseDao(SessionFactory factory) {
        super(factory);
    }

    public SubsCourse find(String tgId) {
        return findValue(SubsCourse.class, tgId);
    }

    public Collection<SubsCourse> findNotNotified(int limit) {
        return useSession(session -> {
            Query<?> q = session.createQuery("from SubsCourse where notified = false");
            q.setMaxResults(limit);
            return (Collection<SubsCourse>) q.list();
        });
    }

    public void save(SubsCourse sub) {
        withSession(session -> session.save(sub));
    }

    public void update(Collection<SubsCourse> subs) {
        withSession(session -> {
            int i = 0;
            for (SubsCourse sub : subs) {
                session.update(sub);

                if (i % 50 == 0) {
                    session.flush();
                    session.clear();
                }

                i++;
            }
        });
    }

    public void resetNotications(Collection<String> keys) {
        withSession(session -> session.createQuery("update SubsCourse " +
                "set notified = false " +
                "where schedule_key in (:keys)")
                .setParameterList("keys", keys)
                .executeUpdate());
    }

    public boolean delete(String tgId) {
        int res = execUpdate("delete from SubsCourse where tg_id = :tg_id",
                q -> q.setParameter("tg_id", tgId));
        return res > 0;
    }

}
