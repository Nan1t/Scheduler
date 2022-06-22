package edu.zieit.scheduler.server.entity.user;

import java.util.List;

public class UsersResponse {

    private List<UserEntity> users;

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }
}
