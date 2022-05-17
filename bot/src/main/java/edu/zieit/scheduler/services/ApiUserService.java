package edu.zieit.scheduler.services;

import edu.zieit.scheduler.persistence.dao.ApiUserDao;

public class ApiUserService {

    private final ApiUserDao dao;

    public ApiUserService(ApiUserDao dao) {
        this.dao = dao;
    }


}
