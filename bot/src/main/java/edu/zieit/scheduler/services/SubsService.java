package edu.zieit.scheduler.services;

import edu.zieit.scheduler.api.NamespacedKey;
import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.persistence.TeacherNotice;
import edu.zieit.scheduler.persistence.dao.NoticesDao;
import edu.zieit.scheduler.persistence.dao.PointsSubsDao;
import edu.zieit.scheduler.persistence.dao.CourseSubsDao;
import edu.zieit.scheduler.persistence.dao.TeacherSubsDao;
import edu.zieit.scheduler.persistence.subscription.SubscriptionPoints;
import edu.zieit.scheduler.persistence.subscription.SubscriptionCourse;
import edu.zieit.scheduler.persistence.subscription.SubscriptionTeacher;

public final class SubsService {

    private final TeacherSubsDao teacherDao;
    private final CourseSubsDao coursesDao;
    private final PointsSubsDao pointsDao;
    private final NoticesDao noticesDao;

    public SubsService(TeacherSubsDao teacherDao, CourseSubsDao coursesDao, PointsSubsDao pointsDao, NoticesDao noticesDao) {
        this.teacherDao = teacherDao;
        this.coursesDao = coursesDao;
        this.pointsDao = pointsDao;
        this.noticesDao = noticesDao;
    }

    public SubscriptionTeacher getTeacherSubs(String chatId) {
        return teacherDao.find(chatId);
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

    public SubscriptionCourse getCourseSubs(String chatId) {
        return coursesDao.find(chatId);
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
