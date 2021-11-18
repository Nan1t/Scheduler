package edu.zieit.scheduler.persistence.dao;

import edu.zieit.scheduler.persistence.subscription.SubscriptionCourse;
import org.hibernate.SessionFactory;

public class CourseSubsDao extends Dao {

    public CourseSubsDao(SessionFactory factory) {
        super(factory);
    }

    public SubscriptionCourse find(String tgId) {
        return findValue(SubscriptionCourse.class, tgId);
    }

    public void save(SubscriptionCourse sub) {
        withSession(session -> session.saveOrUpdate(sub));
    }

    public boolean delete(String tgId) {
        int res = execUpdate("delete from SubscriptionCourse where tg_id = :tg_id",
                q -> q.setParameter("tg_id", tgId));
        return res > 0;
    }

}
