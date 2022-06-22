package edu.zieit.scheduler.server.entity.user;

import java.util.List;

public class SessionsResponse {

    private List<SessionEntity> sessions;

    public List<SessionEntity> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionEntity> sessions) {
        this.sessions = sessions;
    }
}
