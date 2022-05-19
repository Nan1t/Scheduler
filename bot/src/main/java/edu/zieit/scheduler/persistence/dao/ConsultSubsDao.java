package edu.zieit.scheduler.persistence.dao;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.persistence.Dao;
import edu.zieit.scheduler.persistence.subscription.SubscriptionConsult;
import edu.zieit.scheduler.persistence.subscription.SubscriptionTeacher;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Collection;

public class ConsultSubsDao extends Dao {

    @Inject
    public ConsultSubsDao(SessionFactory factory) {
        super(factory);
    }

    public SubscriptionConsult find(String tgId) {
        return findValue(SubscriptionConsult.class, tgId);
    }

    public Collection<SubscriptionConsult> findNotMailed(int limit) {
        return useSession(session -> {
            Query<?> q = session.createQuery("from SubscriptionConsult where received_mailing = false");
            q.setMaxResults(limit);
            return (Collection<SubscriptionConsult>) q.list();
        });
    }

    public void save(SubscriptionConsult sub) {
        withSession(session -> session.saveOrUpdate(sub));
    }

    public void update(Collection<SubscriptionConsult> subs) {
        withSession(session -> {
            int i = 0;
            for (SubscriptionConsult sub : subs) {
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
        withSession(session -> session.createQuery("update SubscriptionConsult set received_mailing = false")
                .executeUpdate());
    }

    public boolean delete(String tgId) {
        int res = execUpdate("delete from SubscriptionConsult where tg_id = :tg_id", q -> q.setParameter("tg_id", tgId));
        return res > 0;
    }

}
