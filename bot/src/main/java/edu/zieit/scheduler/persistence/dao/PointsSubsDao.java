package edu.zieit.scheduler.persistence.dao;

import edu.zieit.scheduler.api.persistence.Dao;
import edu.zieit.scheduler.persistence.subscription.SubscriptionPoints;
import org.hibernate.SessionFactory;

public class PointsSubsDao extends Dao {

    public PointsSubsDao(SessionFactory factory) {
        super(factory);
    }

    public SubscriptionPoints find(String tgId) {
        return findValue(SubscriptionPoints.class, tgId);
    }

    public void save(SubscriptionPoints sub) {
        withSession(session -> session.saveOrUpdate(sub));
    }

    public boolean delete(String tgId) {
        int res = execUpdate("delete from SubscriptionPoints where tg_id = :tg_id",
                q -> q.setParameter("tg_id", tgId));
        return res > 0;
    }

}
