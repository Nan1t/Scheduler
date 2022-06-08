package edu.zieit.scheduler.persistence.entity;

import edu.zieit.scheduler.api.Person;

import javax.persistence.*;

@Entity
@Table(name = "subs_points")
public class SubsPoint {

    @Column(name = "tg_id")
    private String tgId;

    @Column(name = "first_name")
    private String fistName;

    @Column(name = "last_name")
    private String lastName;
    private String patronymic;

    private String password;

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

    public String getFistName() {
        return fistName;
    }

    public void setFistName(String fistName) {
        this.fistName = fistName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Person getPerson() {
        return Person.simple(fistName, lastName, patronymic);
    }
}
