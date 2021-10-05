package edu.zieit.scheduler.data.subscription;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.data.types.PersonDescriptor;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "subs_teachers")
public class SubscriptionTeacher implements Serializable {

    @Id
    @Column(name = "tg_id")
    private String telegramId;

    @Type(type = "edu.zieit.scheduler.data.types.PersonDescriptor")
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
