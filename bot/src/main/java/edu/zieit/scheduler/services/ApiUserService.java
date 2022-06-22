package edu.zieit.scheduler.services;

import com.google.inject.Inject;
import edu.zieit.scheduler.persistence.dao.ApiUserDao;
import edu.zieit.scheduler.persistence.entity.ApiSession;
import edu.zieit.scheduler.persistence.entity.ApiUser;
import edu.zieit.scheduler.util.BCrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;

public final class ApiUserService {

    private static final Logger logger = LogManager.getLogger(ApiUserService.class);

    private final ApiUserDao userDao;

    @Inject
    public ApiUserService(ApiUserDao userDao) {
        this.userDao = userDao;
        createDefaultUser();
    }

    @Nullable
    public ApiUser findUser(String login) {
        return userDao.getUser(login);
    }

    public List<ApiUser> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Nullable
    public ApiSession findSession(String accessToken) {
        return userDao.getSession(accessToken);
    }

    public List<ApiSession> getAllSessions() {
        return userDao.getAllSessions();
    }

    public void createUser(String login, String password, boolean isAdmin) {
        ApiUser user = new ApiUser();
        String salt = BCrypt.gensalt();
        String passwHash = BCrypt.hashpw(password, salt);

        user.setAdmin(isAdmin);
        user.setLogin(login);
        user.setSalt(salt);
        user.setPassword(passwHash);

        saveUser(user);
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

    private void createDefaultUser() {
        long count = userDao.countUsers();

        if (count < 1) {
            createUser("admin", "admin", true);
            logger.info("Created default admin user");
        }
    }
}
