package edu.zieit.scheduler.data.subscription;

import edu.zieit.scheduler.api.NamespaceKey;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "subs_students")
public class SubscriptionStudent {

    @Id
    @Column(name = "tg_id")
    private String telegramId;
    @Type(type = "edu.zieit.scheduler.data.types.NamespaceKeyType")
    @Column(name = "schedule_key")
    private NamespaceKey scheduleKey;

    public String getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }

    public NamespaceKey getScheduleKey() {
        return scheduleKey;
    }

    public void setScheduleKey(NamespaceKey scheduleKey) {
        this.scheduleKey = scheduleKey;
    }
}
