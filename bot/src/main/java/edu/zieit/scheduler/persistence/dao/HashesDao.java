package edu.zieit.scheduler.persistence.dao;

import edu.zieit.scheduler.api.persistence.Dao;
import edu.zieit.scheduler.persistence.ScheduleHash;
import org.hibernate.SessionFactory;

public class HashesDao extends Dao {

    public HashesDao(SessionFactory factory) {
        super(factory);
    }

    public ScheduleHash find(String fileName) {
        ScheduleHash hash = findValue(ScheduleHash.class, fileName);

        if (hash == null) {
            hash = new ScheduleHash();
            hash.setFileName(fileName);
            hash.setHash(null);
        }

        return hash;
    }

    public void save(ScheduleHash sub) {
        withSession(session -> session.saveOrUpdate(sub));
    }

    public void delete(ScheduleHash sub) {
        withSession(session -> session.delete(sub));
    }

}
