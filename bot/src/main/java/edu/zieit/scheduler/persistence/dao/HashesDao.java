package edu.zieit.scheduler.persistence.dao;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.persistence.Dao;
import edu.zieit.scheduler.persistence.entity.ScheduleHash;
import org.hibernate.SessionFactory;

public class HashesDao extends Dao {

    @Inject
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

    public void save(ScheduleHash hash) {
        withSession(session -> session.saveOrUpdate(hash));
    }

    public void delete(ScheduleHash hash) {
        withSession(session -> session.delete(hash));
    }

}
