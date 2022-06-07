package edu.zieit.scheduler.persistence.dao;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.persistence.Dao;
import org.hibernate.SessionFactory;

public class NoticesDao extends Dao {

    @Inject
    public NoticesDao(SessionFactory factory) {
        super(factory);
    }

    public TeacherNotice find(String tgId) {
        return findValue(TeacherNotice.class, tgId);
    }

    public void save(TeacherNotice notice) {
        withSession(session -> session.saveOrUpdate(notice));
    }

    public void delete(String tgId) {
        execUpdate("delete from TeacherNotice where tg_id = :tg_id",
                q -> q.setParameter("tg_id", tgId));
    }

}
