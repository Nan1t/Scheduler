package edu.zieit.scheduler.persistence.dao;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.persistence.Dao;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Collection;

public class TeacherSubsDao extends Dao {

    @Inject
    public TeacherSubsDao(SessionFactory factory) {
        super(factory);
    }

    public SubscriptionTeacher find(String tgId) {
        return findValue(SubscriptionTeacher.class, tgId);
    }

    public Collection<SubscriptionTeacher> findNotMailed(int limit) {
        return useSession(session -> {
            Query<?> q = session.createQuery("from SubscriptionTeacher where received_mailing = false");
            q.setMaxResults(limit);
            return (Collection<SubscriptionTeacher>) q.list();
        });
    }

    public void save(SubscriptionTeacher sub) {
        withSession(session -> session.saveOrUpdate(sub));
    }

    public void update(Collection<SubscriptionTeacher> subs) {
        withSession(session -> {
            int i = 0;
            for (SubscriptionTeacher sub : subs) {
                session.update(sub);

                if (i % 50 == 0) {
                    session.flush();
                    session.clear();
                }

                i++;
            }
        });
    }

    public void resetMailing() {
        withSession(session -> session.createQuery("update SubscriptionTeacher set received_mailing = false")
                .executeUpdate());
    }

    public boolean delete(String tgId) {
        int res = execUpdate("delete from SubscriptionTeacher where tg_id = :tg_id", q -> q.setParameter("tg_id", tgId));
        return res > 0;
    }

}
