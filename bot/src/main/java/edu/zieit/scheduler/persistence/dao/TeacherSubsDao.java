package edu.zieit.scheduler.persistence.dao;

import edu.zieit.scheduler.persistence.subscription.SubscriptionTeacher;
import org.hibernate.SessionFactory;

import java.util.Collection;

public class TeacherSubsDao extends Dao {

    public TeacherSubsDao(SessionFactory factory) {
        super(factory);
    }

    public SubscriptionTeacher find(String tgId) {
        return findValue(SubscriptionTeacher.class, tgId);
    }

    public Collection<SubscriptionTeacher> getWithLimit(int from, int count) {
        return getList(SubscriptionTeacher.class, from, count);
    }

    public void save(SubscriptionTeacher sub) {
        withSession(session -> session.saveOrUpdate(sub));
    }

    public boolean delete(String tgId) {
        int res = execUpdate("delete from SubscriptionTeacher where tg_id = :tg_id", q -> q.setParameter("tg_id", tgId));
        return res > 0;
    }

}
