package edu.zieit.scheduler.persistence.dao;

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

    public void delete(SubscriptionPoints sub) {
        withSession(session -> session.delete(sub));
    }

}
