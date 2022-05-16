package edu.zieit.scheduler.config;

import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

public final class MainConfig extends AbstractConfig {

    private Properties dbProperties;

    private String tgBotName;
    private String tgToken;

    private Pattern regexTeacherDefault;
    private Pattern regexTeacherInline;
    private Pattern regexClassroom;

    private String pointsUrl;
    private String pointsLoginQuery;
    private int pointsLoginSuccessCode;
    private int pointsTimeout;

    private int threadPoolSize;

    private boolean enableRest;
    private int restApiPort;

    public MainConfig(Path rootDir) {
        super(rootDir, "/config.yml", Map.of());
    }

    @Override
    protected void load() {
        dbProperties = loadProperties(conf.getNode("database"));
        tgBotName = conf.getNode("telegram", "bot_name").getString();
        tgToken = conf.getNode("telegram", "token").getString();

        regexTeacherDefault = Pattern.compile(conf.getNode("regex", "teacher_default").getString(""));
        regexTeacherInline = Pattern.compile(conf.getNode("regex", "teacher_inline").getString(""));
        regexClassroom = Pattern.compile(conf.getNode("regex", "classroom").getString(""));

        pointsUrl = conf.getNode("points", "url").getString();
        pointsLoginQuery = conf.getNode("points", "login_query").getString();
        pointsLoginSuccessCode = conf.getNode("points", "login_success").getInt();
        pointsTimeout = conf.getNode("points", "timeout").getInt() * 1000;

        threadPoolSize = conf.getNode("thread_pool_size").getInt(4);
    }

    public Properties getDbProperties() {
        return dbProperties;
    }

    public String getTgBotName() {
        return tgBotName;
    }

    public String getTgToken() {
        return tgToken;
    }

    public Pattern getRegexTeacherDefault() {
        return regexTeacherDefault;
    }

    public Pattern getRegexTeacherInline() {
        return regexTeacherInline;
    }

    public Pattern getRegexClassroom() {
        return regexClassroom;
    }

    public String getPointsUrl() {
        return pointsUrl;
    }

    public String getPointsLoginQuery() {
        return pointsLoginQuery;
    }

    public int getPointsLoginSuccessCode() {
        return pointsLoginSuccessCode;
    }

    public int getPointsTimeout() {
        return pointsTimeout;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public boolean isEnableRest() {
        return enableRest;
    }

    public int getRestApiPort() {
        return restApiPort;
    }
}
