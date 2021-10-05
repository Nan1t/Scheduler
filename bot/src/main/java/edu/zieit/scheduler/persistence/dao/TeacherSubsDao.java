package edu.zieit.scheduler.persistence.dao;

import edu.zieit.scheduler.persistence.subscription.SubscriptionTeacher;
import org.hibernate.SessionFactory;

public class TeacherSubsDao extends Dao {

    public TeacherSubsDao(SessionFactory factory) {
        super(factory);
    }

    public SubscriptionTeacher find(String tgId) {
        return findValue(SubscriptionTeacher.class, tgId);
    }

    public void save(SubscriptionTeacher sub) {
        withSession(session -> session.saveOrUpdate(sub));
    }

    public void delete(SubscriptionTeacher sub) {
        withSession(session -> session.delete(sub));
    }

}
