package edu.zieit.scheduler.data.types;

import edu.zieit.scheduler.api.Person;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class PersonType implements UserType<Person> {

    public PersonType() { }

    @Override
    public int[] sqlTypes() {
        return new int[] {Types.VARCHAR, Types.CLOB};
    }

    @Override
    public Class<Person> returnedClass() {
        return Person.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Person nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String patronymic = rs.getString("patronymic");
        return Person.simple(firstName, lastName, patronymic);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Person person, int index, SharedSessionContractImplementor session) throws SQLException {
        st.setString(index, person.firstName());
        st.setString(index+1, person.lastName());
        st.setString(index+2, person.patronymic());
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
