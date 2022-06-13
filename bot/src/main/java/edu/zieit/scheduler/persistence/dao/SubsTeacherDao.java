package edu.zieit.scheduler.persistence.dao;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.persistence.Dao;
import edu.zieit.scheduler.persistence.entity.SubsTeacher;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Collection;

public class SubsTeacherDao extends Dao {

    @Inject
    public SubsTeacherDao(SessionFactory factory) {
        super(factory);
    }

    public SubsTeacher find(String tgId) {
        return findValue(SubsTeacher.class, tgId);
    }

    public Collection<SubsTeacher> findNotNotified(int limit) {
        return useSession(session -> {
            Query<?> q = session.createQuery("from SubsTeacher where notified = false");
            q.setMaxResults(limit);
            return (Collection<SubsTeacher>) q.list();
        });
    }

    public void save(SubsTeacher sub) {
        withSession(session -> session.save(sub));
    }

    public void update(Collection<SubsTeacher> subs) {
        withSession(session -> {
            int i = 0;
            for (SubsTeacher sub : subs) {
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
        withSession(session -> session.createQuery("update SubsTeacher set notified = false")
                .executeUpdate());
    }

    public boolean delete(String tgId) {
        int res = execUpdate("delete from SubsTeacher where tg_id = :tg_id", q -> q.setParameter("tg_id", tgId));
        return res > 0;
    }

    public long count() {
        return useSession(session -> {
            Query<?> query = session.createQuery("select count(*) from SubsTeacher");
            return (Long) query.uniqueResult();
        });
    }

}
