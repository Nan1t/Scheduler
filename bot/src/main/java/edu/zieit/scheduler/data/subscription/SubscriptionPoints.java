package edu.zieit.scheduler.data.subscription;

import edu.zieit.scheduler.api.Person;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "subs_points")
public class SubscriptionPoints {

    @Id
    @Column(name = "tg_id")
    private String telegramId;
    private Person person;

    public String getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }

    public Person getTeacher() {
        return person;
    }

    public void setTeacher(Person person) {
        this.person = person;
    }
}
