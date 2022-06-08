package edu.zieit.scheduler.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class BotUser {

    @Id
    @Column(name = "tg_id")
    private String tgId;
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
    private boolean notifications;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private SubsTeacher subsTeacher;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private SubsTeacher subsCourse;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private SubsTeacher subsGroup;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private SubsTeacher subsPoint;

    public String getTgId() {
        return tgId;
    }

    public void setTgId(String tgId) {
        this.tgId = tgId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public SubsTeacher getSubsTeacher() {
        return subsTeacher;
    }

    public void setSubsTeacher(SubsTeacher subsTeacher) {
        this.subsTeacher = subsTeacher;
    }

    public SubsTeacher getSubsCourse() {
        return subsCourse;
    }

    public void setSubsCourse(SubsTeacher subsCourse) {
        this.subsCourse = subsCourse;
    }

    public SubsTeacher getSubsGroup() {
        return subsGroup;
    }

    public void setSubsGroup(SubsTeacher subsGroup) {
        this.subsGroup = subsGroup;
    }

    public SubsTeacher getSubsPoint() {
        return subsPoint;
    }

    public void setSubsPoint(SubsTeacher subsPoint) {
        this.subsPoint = subsPoint;
    }
}
