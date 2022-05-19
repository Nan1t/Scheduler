package edu.zieit.scheduler.services;

import com.google.inject.Inject;
import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.persistence.TeacherNotice;
import edu.zieit.scheduler.persistence.dao.*;
import edu.zieit.scheduler.persistence.subscription.*;

import java.util.Collection;
import java.util.stream.Collectors;

public final class SubsService {

    private final TeacherSubsDao teacherDao;
    private final ConsultSubsDao consultDao;
    private final CourseSubsDao coursesDao;
    private final PointsSubsDao pointsDao;
    private final NoticesDao noticesDao;
    private final GroupSubsDao groupsDao;

    @Inject
    public SubsService(TeacherSubsDao teacherDao, ConsultSubsDao consultDao, CourseSubsDao coursesDao,
                       PointsSubsDao pointsDao, NoticesDao noticesDao, GroupSubsDao groupsDao) {
        this.teacherDao = teacherDao;
        this.consultDao = consultDao;
        this.coursesDao = coursesDao;
        this.pointsDao = pointsDao;
        this.noticesDao = noticesDao;
        this.groupsDao = groupsDao;
    }

    /* Teachers */

    public SubscriptionTeacher getTeacherSubs(String chatId) {
        return teacherDao.find(chatId);
    }

    public Collection<SubscriptionTeacher> getNotMailedTeacherSubs() {
        return teacherDao.findNotMailed(30);
    }

    public void subscribeTeacher(String chatId, Person teacher) {
        SubscriptionTeacher sub = new SubscriptionTeacher();
        sub.setTelegramId(chatId);
        sub.setTeacher(teacher);
        teacherDao.save(sub);
    }

    public boolean unsubscribeTeacher(String chatId) {
        return teacherDao.delete(chatId);
    }

    public void resetTeacherMailing() {
        teacherDao.resetMailing();
    }

    public void updateTeacherSubs(Collection<SubscriptionTeacher> subs) {
        teacherDao.update(subs);
    }

    public boolean toggleNotices(String chatId) {
        TeacherNotice notice = noticesDao.find(chatId);

        if (notice == null) {
            notice = new TeacherNotice();
            notice.setTelegramId(chatId);
            notice.setEnabled(true);
            noticesDao.save(notice);
            return true;
        } else {
            noticesDao.delete(chatId);
            return false;
        }
    }

    /* Consultations */

    public SubscriptionConsult getConsultSubs(String chatId) {
        return consultDao.find(chatId);
    }

    public Collection<SubscriptionConsult> getNotMailedConsultSubs() {
        return consultDao.findNotMailed(30);
    }

    public void subscribeConsult(String chatId, Person teacher) {
        SubscriptionConsult sub = new SubscriptionConsult();
        sub.setTelegramId(chatId);
        sub.setTeacher(teacher);
        consultDao.save(sub);
    }

    public boolean unsubscribeConsult(String chatId) {
        return consultDao.delete(chatId);
    }

    public void resetConsultMailing() {
        consultDao.resetMailing();
    }

    public void updateConsultSubs(Collection<SubscriptionConsult> subs) {
        consultDao.update(subs);
    }

    /* Courses */

    public SubscriptionCourse getCourseSubs(String chatId) {
        return coursesDao.find(chatId);
    }

    public Collection<SubscriptionCourse> getNotMailedCourseSubs() {
        return coursesDao.findNotMailed(30);
    }

    public void subscribeCourse(String chatId, NamespacedKey scheduleKey) {
        SubscriptionCourse sub = new SubscriptionCourse();
        sub.setTelegramId(chatId);
        sub.setScheduleKey(scheduleKey);
        coursesDao.save(sub);
    }

    public boolean unsubscribeCourse(String chatId) {
        return coursesDao.delete(chatId);
    }

    public void resetCourseMailing(Collection<NamespacedKey> keys) {
        coursesDao.resetMailing(keys.stream()
                .map(NamespacedKey::toString)
                .collect(Collectors.toList()));
    }

    public void updateCourseSubs(Collection<SubscriptionCourse> subs) {
        coursesDao.update(subs);
    }

    /* Groups */

    public SubscriptionGroup getGroupSubs(String chatId) {
        return groupsDao.find(chatId);
    }

    public Collection<SubscriptionGroup> getNotMailedGroupSubs() {
        return groupsDao.findNotMailed(30);
    }

    public void subscribeGroup(String chatId, String group) {
        SubscriptionGroup sub = new SubscriptionGroup();
        sub.setTelegramId(chatId);
        sub.setGroupName(group);
        groupsDao.save(sub);
    }

    public boolean unsubscribeGroup(String chatId) {
        return groupsDao.delete(chatId);
    }

    public void resetGroupMailing(Collection<String> groups) {
        groupsDao.resetMailing(groups);
    }

    public void updateGroupSubs(Collection<SubscriptionGroup> subs) {
        groupsDao.update(subs);
    }

    /* Points */

    public SubscriptionPoints getPointsSubs(String chatId) {
        return pointsDao.find(chatId);
    }

    public void subscribePoints(String chatId, Person student, String password) {
        SubscriptionPoints sub = new SubscriptionPoints();
        sub.setTelegramId(chatId);
        sub.setPerson(student);
        sub.setPassword(password);
        pointsDao.save(sub);
    }

    public boolean unsubscribePoints(String chatId) {
        return pointsDao.delete(chatId);
    }

}
