package edu.zieit.scheduler.persistence.dao;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.persistence.Dao;
import edu.zieit.scheduler.persistence.entity.SubsPoint;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class SubsPointsDao extends Dao {

    @Inject
    public SubsPointsDao(SessionFactory factory) {
        super(factory);
    }

    public SubsPoint find(String tgId) {
        return findValue(SubsPoint.class, tgId);
    }

    public void save(SubsPoint sub) {
        withSession(session -> session.save(sub));
    }

    public boolean delete(String tgId) {
        int res = execUpdate("delete from SubsPoint where tg_id = :tg_id",
                q -> q.setParameter("tg_id", tgId));
        return res > 0;
    }

    public long count() {
        return useSession(session -> {
            Query<?> query = session.createQuery("select count(*) from SubsPoint");
            return (Long) query.uniqueResult();
        });
    }
}
