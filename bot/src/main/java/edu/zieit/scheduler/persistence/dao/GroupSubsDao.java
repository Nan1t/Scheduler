package edu.zieit.scheduler.persistence.dao;

import edu.zieit.scheduler.persistence.subscription.SubscriptionGroup;
import edu.zieit.scheduler.persistence.subscription.SubscriptionTeacher;
import org.hibernate.SessionFactory;

import java.util.Collection;

public class GroupSubsDao extends Dao {

    public GroupSubsDao(SessionFactory factory) {
        super(factory);
    }

    public SubscriptionGroup find(String tgId) {
        return findValue(SubscriptionGroup.class, tgId);
    }

    public Collection<SubscriptionGroup> getWithLimit(int from, int count) {
        return getList(SubscriptionGroup.class, from, count);
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
