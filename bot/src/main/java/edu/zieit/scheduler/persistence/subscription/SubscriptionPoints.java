package edu.zieit.scheduler.persistence.subscription;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.persistence.descriptors.PersonDescriptor;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "subs_points")
@TypeDef(name = "PersonDescriptor", typeClass = PersonDescriptor.class)
public class SubscriptionPoints {

    @Id
    @Column(name = "tg_id")
    private String telegramId;

    @Type(type = "PersonDescriptor")
    @Columns(columns = {
            @Column(name = "first_name"),
            @Column(name = "last_name"),
            @Column(name = "patronymic")
    })
    private Person person;
    private String password;

    public String getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
