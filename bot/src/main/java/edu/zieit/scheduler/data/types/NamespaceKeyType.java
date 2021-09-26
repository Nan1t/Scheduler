package edu.zieit.scheduler.data.types;

import edu.zieit.scheduler.api.NamespaceKey;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class NamespaceKeyType implements UserType<NamespaceKey> {

    public NamespaceKeyType() { }

    @Override
    public int[] sqlTypes() {
        return new int[Types.VARCHAR];
    }

    @Override
    public Class<NamespaceKey> returnedClass() {
        return NamespaceKey.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x instanceof NamespaceKey k1 && y instanceof NamespaceKey k2) {
            return k1.equals(k2);
        }
        return false;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public NamespaceKey nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        String str = rs.getString(position);
        if (str != null)
            return NamespaceKey.parse(str);
        return null;
    }

    @Override
    public void nullSafeSet(PreparedStatement st, NamespaceKey value, int index, SharedSessionContractImplementor session) throws SQLException {
        st.setString(index, value.toString());
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}
