package edu.zieit.scheduler.persistence.dao;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.persistence.Dao;
import edu.zieit.scheduler.persistence.entity.SubsConsult;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Collection;

public class SubsConsultDao extends Dao {

    @Inject
    public SubsConsultDao(SessionFactory factory) {
        super(factory);
    }

    public SubsConsult find(String tgId) {
        return findValue(SubsConsult.class, tgId);
    }

    public Collection<SubsConsult> findNotNotified(int limit) {
        return useSession(session -> {
            Query<?> q = session.createQuery("from SubsConsult where notified = false");
            q.setMaxResults(limit);
            return (Collection<SubsConsult>) q.list();
        });
    }

    public void save(SubsConsult sub) {
        withSession(session -> session.save(sub));
    }

    public void update(Collection<SubsConsult> subs) {
        withSession(session -> {
            int i = 0;
            for (SubsConsult sub : subs) {
                session.update(sub);

                if (i % 50 == 0) {
                    session.flush();
                    session.clear();
                }

                i++;
            }
        });
    }

    public void resetNotifications() {
        withSession(session -> session.createQuery("update SubsConsult set notified = false")
                .executeUpdate());
    }

    public boolean delete(String tgId) {
        int res = execUpdate("delete from SubsConsult where tg_id = :tg_id", q -> q.setParameter("tg_id", tgId));
        return res > 0;
    }

    public long count() {
        return useSession(session -> {
            Query<?> query = session.createQuery("select count(*) from SubsConsult");
            return (Long) query.uniqueResult();
        });
    }

}
