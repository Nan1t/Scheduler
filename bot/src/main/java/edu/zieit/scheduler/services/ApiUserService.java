package edu.zieit.scheduler.services;

import com.google.inject.Inject;
import edu.zieit.scheduler.persistence.dao.ApiUserDao;
import edu.zieit.scheduler.persistence.entity.ApiSession;
import edu.zieit.scheduler.persistence.entity.ApiUser;

import javax.annotation.Nullable;

public final class ApiUserService {

    private final ApiUserDao userDao;

    @Inject
    public ApiUserService(ApiUserDao userDao) {
        this.userDao = userDao;
    }

    @Nullable
    public ApiUser findUser(String login) {
        return userDao.getUser(login);
    }

    @Nullable
    public ApiSession findSession(String accessToken) {
        return userDao.getSession(accessToken);
    }

    public void saveUser(ApiUser user) {
        userDao.save(user);
    }

    public void saveSession(ApiSession session) {
        userDao.saveSession(session);
    }

    public void deleteUser(ApiUser user) {
        userDao.delete(user);
    }

    public void endSession(ApiSession session) {
        userDao.deleteSession(session);
    }

}
