package edu.zieit.scheduler.services;

import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.persistence.dao.PointsSubsDao;
import edu.zieit.scheduler.persistence.dao.StudentSubsDao;
import edu.zieit.scheduler.persistence.dao.TeacherSubsDao;
import edu.zieit.scheduler.persistence.subscription.SubscriptionPoints;
import edu.zieit.scheduler.persistence.subscription.SubscriptionStudent;
import edu.zieit.scheduler.persistence.subscription.SubscriptionTeacher;

public final class SubsService {

    private final TeacherSubsDao teacherDao;
    private final StudentSubsDao studentDao;
    private final PointsSubsDao pointsDao;

    public SubsService(TeacherSubsDao teacherDao, StudentSubsDao studentDao, PointsSubsDao pointsDao) {
        this.teacherDao = teacherDao;
        this.studentDao = studentDao;
        this.pointsDao = pointsDao;
    }

    public SubscriptionTeacher getTeacherSubs(String chatId) {
        return teacherDao.find(chatId);
    }

    public void subscribeTeacher(String chatId, Person teacher) {
        SubscriptionTeacher sub = new SubscriptionTeacher();
        sub.setTelegramId(chatId);
        sub.setTeacher(teacher);
        sub.setNotices(false);
        teacherDao.save(sub);
    }

    public void unsubscribeTeacher(String chatId) {
        teacherDao.delete(chatId);
    }

    public void toggleTeacherNotices(String chatId) {
        teacherDao.toggleNotices(chatId);
    }

    public SubscriptionStudent getStudentSubs(String chatId) {
        return studentDao.find(chatId);
    }

    public void subscribeStudent(String chatId, NamespacedKey scheduleKey) {
        SubscriptionStudent sub = new SubscriptionStudent();
        sub.setTelegramId(chatId);
        sub.setScheduleKey(scheduleKey);
        studentDao.save(sub);
    }

    public void unsubscribeStudent(String chatId) {
        studentDao.delete(chatId);
    }

    public SubscriptionPoints getPointsSubs(String chatId) {
        return pointsDao.find(chatId);
    }

    public void subscribePoints(String chatId, Person student) {
        SubscriptionPoints sub = new SubscriptionPoints();
        sub.setTelegramId(chatId);
        sub.setPerson(student);
        pointsDao.save(sub);
    }

    public void unsubscribePoints(String chatId) {
        pointsDao.delete(chatId);
    }

}
