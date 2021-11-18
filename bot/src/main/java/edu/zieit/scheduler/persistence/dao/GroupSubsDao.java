package edu.zieit.scheduler.persistence.dao;

import edu.zieit.scheduler.persistence.subscription.SubscriptionGroup;
import org.hibernate.SessionFactory;

public class GroupSubsDao extends Dao {

    public GroupSubsDao(SessionFactory factory) {
        super(factory);
    }

    public SubscriptionGroup find(String tgId) {
        return findValue(SubscriptionGroup.class, tgId);
    }

    public void save(SubscriptionGroup sub) {
        withSession(session -> session.saveOrUpdate(sub));
    }

    public boolean delete(String tgId) {
        int res = execUpdate("delete from SubscriptionGroup where tg_id = :tg_id",
                q -> q.setParameter("tg_id", tgId));
        return res > 0;
    }

}
