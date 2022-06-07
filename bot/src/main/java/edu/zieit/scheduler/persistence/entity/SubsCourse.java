package edu.zieit.scheduler.persistence.entity;

import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.persistence.descriptors.NamespacedKeyDescriptor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Table(name = "subs_teachers")
@TypeDef(name = "NamespacedKeyDescriptor", typeClass = NamespacedKeyDescriptor.class)
public class SubsCourse {

    @Column(name = "tg_id")
    private String tgId;

    @Type(type = "NamespacedKeyDescriptor")
    @Column(name = "schedule_key")
    private NamespacedKey scheduleKey;

    private boolean notified = true;

    @OneToOne
    @MapsId
    @JoinColumn(name = "tg_id")
    private User user;

    public String getTgId() {
        return tgId;
    }

    public void setTgId(String tgId) {
        this.tgId = tgId;
    }

    public NamespacedKey getScheduleKey() {
        return scheduleKey;
    }

    public void setScheduleKey(NamespacedKey scheduleKey) {
        this.scheduleKey = scheduleKey;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
