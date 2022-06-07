package edu.zieit.scheduler.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "subs_groups")
public class SubsGroup {

    @Column(name = "tg_id")
    private String tgId;
    private String group;
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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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
