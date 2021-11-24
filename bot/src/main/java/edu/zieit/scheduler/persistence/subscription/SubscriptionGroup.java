package edu.zieit.scheduler.persistence.subscription;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "subs_groups")
public class SubscriptionGroup {

    @Id
    @Column(name = "tg_id")
    private String telegramId;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "received_mailing")
    private boolean receivedMailing = true;

    public String getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(String telegramId) {
        this.telegramId = telegramId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isReceivedMailing() {
        return receivedMailing;
    }

    public void setReceivedMailing(boolean receivedMailing) {
        this.receivedMailing = receivedMailing;
    }
}
