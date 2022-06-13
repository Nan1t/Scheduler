package edu.zieit.scheduler.services;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.persistence.dao.*;
import edu.zieit.scheduler.persistence.entity.*;

import java.util.Collection;
import java.util.stream.Collectors;

public final class SubsService {

    private final UserDao userDao;
    private final SubsTeacherDao teacherDao;
    private final SubsConsultDao consultDao;
    private final SubsCourseDao coursesDao;
    private final SubsPointsDao pointsDao;
    private final SubsGroupDao groupsDao;

    @Inject
    public SubsService(
            UserDao userDao,
            SubsTeacherDao teacherDao, 
            SubsConsultDao consultDao, 
            SubsCourseDao coursesDao,
            SubsPointsDao pointsDao,
            SubsGroupDao groupsDao
    ) {
        this.userDao = userDao;
        this.teacherDao = teacherDao;
        this.consultDao = consultDao;
        this.coursesDao = coursesDao;
        this.pointsDao = pointsDao;
        this.groupsDao = groupsDao;
    }

    /* Users */

    public BotUser getOrCreateUser(
            String tgId,
            String username,
            String firstName,
            String lastName
    ) {
        BotUser user = userDao.find(tgId);

        if (user == null) {
            user = new BotUser();
            user.setTgId(tgId);
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);

            userDao.save(user);
        }

        return user;
    }

    /* Teachers */

    public SubsTeacher getTeacherSubs(String chatId) {
        return teacherDao.find(chatId);
    }

    public Collection<SubsTeacher> getNotNotifiedTeacherSubs() {
        return teacherDao.findNotNotified(30);
    }

    public void subscribeTeacher(BotUser user, Person teacher) {
        SubsTeacher sub = new SubsTeacher();
        sub.setTgId(user.getTgId());
        sub.setFistName(teacher.firstName());
        sub.setLastName(teacher.lastName());
        sub.setPatronymic(teacher.patronymic());
        sub.setUser(user);
        teacherDao.save(sub);
    }

    public boolean unsubscribeTeacher(BotUser user) {
        user.setSubsTeacher(null);
        return teacherDao.delete(user.getTgId());
    }

    public void resetTeacherNotifications() {
        teacherDao.resetNotifications();
    }

    public void updateTeacherSubs(Collection<SubsTeacher> subs) {
        teacherDao.update(subs);
    }

    /* Consultations */

    public SubsConsult getConsultSubs(String chatId) {
        return consultDao.find(chatId);
    }

    public Collection<SubsConsult> getNotNotifiedConsultSubs() {
        return consultDao.findNotNotified(30);
    }

    public void subscribeConsult(BotUser user, Person teacher) {
        SubsConsult sub = new SubsConsult();
        sub.setTgId(user.getTgId());
        sub.setFistName(teacher.firstName());
        sub.setLastName(teacher.lastName());
        sub.setPatronymic(teacher.patronymic());
        sub.setUser(user);
        consultDao.save(sub);
    }

    public boolean unsubscribeConsult(BotUser user) {
        user.setSubsConsult(null);
        return consultDao.delete(user.getTgId());
    }

    public void resetConsultNotifications() {
        consultDao.resetNotifications();
    }

    public void updateConsultSubs(Collection<SubsConsult> subs) {
        consultDao.update(subs);
    }

    /* Courses */

    public SubsCourse getCourseSubs(String chatId) {
        return coursesDao.find(chatId);
    }

    public Collection<SubsCourse> getNotNotifiedCourseSubs() {
        return coursesDao.findNotNotified(30);
    }

    public void subscribeCourse(BotUser user, NamespacedKey scheduleKey) {
        SubsCourse sub = new SubsCourse();
        sub.setTgId(user.getTgId());
        sub.setScheduleKey(scheduleKey);
        sub.setUser(user);
        coursesDao.save(sub);
    }

    public boolean unsubscribeCourse(BotUser user) {
        user.setSubsCourse(null);
        return coursesDao.delete(user.getTgId());
    }

    public void resetCourseNotifications(Collection<NamespacedKey> keys) {
        coursesDao.resetNotications(keys.stream()
                .map(NamespacedKey::toString)
                .collect(Collectors.toList()));
    }

    public void updateCourseSubs(Collection<SubsCourse> subs) {
        coursesDao.update(subs);
    }

    /* Groups */

    public SubsGroup getGroupSubs(String chatId) {
        return groupsDao.find(chatId);
    }

    public Collection<SubsGroup> getNotNotifiedGroupSubs() {
        return groupsDao.findNotNotified(30);
    }

    public void subscribeGroup(BotUser user, String group) {
        SubsGroup sub = new SubsGroup();
        sub.setTgId(user.getTgId());
        sub.setGroupName(group);
        sub.setUser(user);
        groupsDao.save(sub);
    }

    public boolean unsubscribeGroup(BotUser user) {
        user.setSubsGroup(null);
        return groupsDao.delete(user.getTgId());
    }

    public void resetGroupNotifications(Collection<String> groups) {
        groupsDao.resetNotifications(groups);
    }

    public void updateGroupSubs(Collection<SubsGroup> subs) {
        groupsDao.update(subs);
    }

    /* Points */

    public SubsPoint getPointsSubs(String chatId) {
        return pointsDao.find(chatId);
    }

    public void subscribePoints(BotUser user, Person student, String password) {
        SubsPoint sub = new SubsPoint();
        sub.setTgId(user.getTgId());
        sub.setFistName(student.firstName());
        sub.setLastName(student.lastName());
        sub.setPatronymic(student.patronymic());
        sub.setPassword(password);
        sub.setUser(user);
        pointsDao.save(sub);
    }

    public boolean unsubscribePoints(BotUser user) {
        user.setSubsPoint(null);
        return pointsDao.delete(user.getTgId());
    }

}
