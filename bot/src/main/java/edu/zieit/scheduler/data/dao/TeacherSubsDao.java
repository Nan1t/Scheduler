package edu.zieit.scheduler.data.dao;

import edu.zieit.scheduler.data.subscription.SubscriptionTeacher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class TeacherSubsDao {

    private final SessionFactory sessionFactory;

    public TeacherSubsDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SubscriptionTeacher find(String tgId) {
        try (Session session = sessionFactory.openSession()){
            return session.get(SubscriptionTeacher.class, tgId);
        }
    }

    public void create(SubscriptionTeacher sub) {
        try (Session session = sessionFactory.openSession()){
            session.beginTransaction();
            session.save(sub);
            session.getTransaction().commit();
        }
    }

}
