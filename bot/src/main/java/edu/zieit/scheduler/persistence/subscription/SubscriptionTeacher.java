package edu.zieit.scheduler.persistence.subscription;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.persistence.descriptors.PersonDescriptor;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "subs_teachers")
@TypeDef(name = "PersonDescriptor", typeClass = PersonDescriptor.class)
public class SubscriptionTeacher implements Serializable {

    @Id
    @Column(name = "tg_id")
    private String telegramId;

    @Type(type = "PersonDescriptor")
    @Columns(columns = {
            @Column(name = "first_name"),
            @Column(name = "last_name"),
            @Column(name = "patronymic")
    })
    private Person teacher;

    private boolean notices;

    public String getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }

    public Person getTeacher() {
        return teacher;
    }

    public void setTeacher(Person teacher) {
        this.teacher = teacher;
    }

    public boolean isNotices() {
        return notices;
    }

    public void setNotices(boolean notices) {
        this.notices = notices;
    }
}
