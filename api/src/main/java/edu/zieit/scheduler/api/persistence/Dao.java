package edu.zieit.scheduler.api.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;

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
        try (Session session = factory.openSession()) {
            Transaction t = session.beginTransaction();
            consumer.accept(session);
            t.commit();
        }
    }

    protected <T> T useSession(Function<Session, T> func) {
        try (Session session = factory.openSession()) {
            return func.apply(session);
        }
    }

    protected int execUpdate(String hql, Consumer<Query<?>> consumer) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            Query<?> query = session.createQuery(hql);
            consumer.accept(query);
            int res = query.executeUpdate();
            session.getTransaction().commit();
            return res;
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
        try (Session session = factory.openSession()) {
            Object val = session.get(type, key);
            return val != null ? (T) val : null;
        }
    }

}
