package edu.zieit.scheduler.persistence.dao;

import edu.zieit.scheduler.persistence.Dao;
import edu.zieit.scheduler.persistence.ScheduleHash;
import org.hibernate.SessionFactory;

public class HashesDao extends Dao {

    public HashesDao(SessionFactory factory) {
        super(factory);
    }

    public ScheduleHash find(String fileName) {
        return findValue(ScheduleHash.class, fileName);
    }

    public void save(ScheduleHash sub) {
        withSession(session -> session.saveOrUpdate(sub));
    }

    public void delete(ScheduleHash sub) {
        withSession(session -> session.delete(sub));
    }

}
