package edu.zieit.scheduler.persistence.dao;

import edu.zieit.scheduler.persistence.subscription.SubscriptionStudent;
import org.hibernate.SessionFactory;

public class StudentSubsDao extends Dao {

    public StudentSubsDao(SessionFactory factory) {
        super(factory);
    }

    public SubscriptionStudent find(String tgId) {
        return findValue(SubscriptionStudent.class, tgId);
    }

    public void save(SubscriptionStudent sub) {
        withSession(session -> session.saveOrUpdate(sub));
    }

    public boolean delete(String tgId) {
        int res = execUpdate("delete from SubscriptionStudent where tg_id = :tg_id",
                q -> q.setParameter("tg_id", tgId));
        return res > 0;
    }

}
