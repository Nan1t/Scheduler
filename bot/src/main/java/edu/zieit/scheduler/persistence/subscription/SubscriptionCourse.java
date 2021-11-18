package edu.zieit.scheduler.persistence.subscription;

import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.persistence.descriptors.NamespacedKeyDescriptor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "subs_courses")
@TypeDef(name = "NamespacedKeyDescriptor", typeClass = NamespacedKeyDescriptor.class)
public class SubscriptionCourse {

    @Id
    @Column(name = "tg_id")
    private String telegramId;

    @Type(type = "NamespacedKeyDescriptor")
    @Column(name = "schedule_key")
    private NamespacedKey scheduleKey;

    public String getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }

    public NamespacedKey getScheduleKey() {
        return scheduleKey;
    }

    public void setScheduleKey(NamespacedKey scheduleKey) {
        this.scheduleKey = scheduleKey;
    }
}
