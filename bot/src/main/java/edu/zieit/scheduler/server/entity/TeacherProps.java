package edu.zieit.scheduler.server.entity;

import java.util.Map;

public class TeacherProps {

    private String url;
    private Map<String, String> associations;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getAssociations() {
        return associations;
    }

    public void setAssociations(Map<String, String> associations) {
        this.associations = associations;
    }
}
