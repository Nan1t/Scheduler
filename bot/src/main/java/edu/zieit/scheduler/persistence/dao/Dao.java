package edu.zieit.scheduler.persistence.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.function.Consumer;

public abstract class Dao {

    protected final SessionFactory factory;

    public Dao(SessionFactory factory) {
        this.factory = factory;
    }

    /**
     * Create new session and start transaction.
     * After consumer code complete, transaction will be automatically committed
     * @param consumer Session callback
     */
    protected void withSession(Consumer<Session> consumer) {
        try (Session session = factory.openSession()){
            session.beginTransaction();
            consumer.accept(session);
            session.getTransaction().commit();
        }
    }

    /**
     * Find some value in table by primary key
     * @param type Object type to map
     * @param key Key value
     * @param <T> Type of returned object
     * @return A persistent instance or null
     */
    protected <T> T findValue(Class<?> type, Serializable key) {
        try (Session session = factory.openSession()){
            return (T) session.get(type, key);
        }
    }

}
