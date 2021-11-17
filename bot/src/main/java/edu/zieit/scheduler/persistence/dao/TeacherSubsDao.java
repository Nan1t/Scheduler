package edu.zieit.scheduler.persistence.dao;

import edu.zieit.scheduler.persistence.subscription.SubscriptionTeacher;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

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

    public void delete(String tgId) {
        withSession(session -> session.createQuery(
                "delete subs_teachers where tg_id = :tg_id")
                .setParameter("tg_id", tgId)
                .executeUpdate());
    }

    public void toggleNotices(String tgId) {
        withSession(session -> session.createQuery(
                "update subs_teachers set notices = !subs_teachers.notices where tg_id = :tg_id")
                .setParameter("tg_id", tgId)
                .executeUpdate());
    }

}
