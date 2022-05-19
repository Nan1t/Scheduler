package edu.zieit.scheduler.services;

import com.google.inject.Inject;
import edu.zieit.scheduler.persistence.dao.ApiSessionDao;
import edu.zieit.scheduler.persistence.dao.ApiUserDao;
import edu.zieit.scheduler.persistence.webapi.ApiSession;

public class ApiUserService {

    private final ApiUserDao userDao;
    private final ApiSessionDao sessionDao;

    @Inject
    public ApiUserService(ApiUserDao userDao, ApiSessionDao sessionDao) {
        this.userDao = userDao;
        this.sessionDao = sessionDao;
    }

    public ApiSession getSession(String accessToken) {
        return sessionDao.find(accessToken);
    }

    public void deleteSession(ApiSession session) {
        sessionDao.delete(session);
    }
}
