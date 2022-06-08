package edu.zieit.scheduler.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "subs_groups")
public class SubsGroup {

    @Id
    @Column(name = "tg_id")
    private String tgId;

    private String groupName;
    private boolean notified = true;

    @OneToOne
    @MapsId
    @JoinColumn(name = "tg_id")
    private BotUser user;

    public String getTgId() {
        return tgId;
    }

    public void setTgId(String tgId) {
        this.tgId = tgId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public BotUser getUser() {
        return user;
    }

    public void setUser(BotUser user) {
        this.user = user;
    }
}
