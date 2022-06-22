package edu.zieit.scheduler.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import edu.zieit.scheduler.api.config.AbstractConfig;

import java.nio.file.Path;
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

    @Inject
    public MainConfig(@Named("appDir") Path rootDir) {
        super(rootDir, "/config.yml");
    }

    @Override
    protected void load() {
        dbProperties = loadProperties(conf.node("database"));
        tgBotName = conf.node("telegram", "bot_name").getString();
        tgToken = conf.node("telegram", "token").getString();

        regexTeacherDefault = Pattern.compile(conf.node("regex", "teacher_default").getString(""));
        regexTeacherInline = Pattern.compile(conf.node("regex", "teacher_inline").getString(""));
        regexClassroom = Pattern.compile(conf.node("regex", "classroom").getString(""));

        pointsUrl = conf.node("points", "url").getString();
        pointsLoginQuery = conf.node("points", "login_query").getString();
        pointsLoginSuccessCode = conf.node("points", "login_success").getInt();
        pointsTimeout = conf.node("points", "timeout").getInt() * 1000;

        threadPoolSize = conf.node("thread_pool_size").getInt(4);

        enableRest = conf.node("rest_api", "enable").getBoolean(false);
        restApiPort = conf.node("rest_api", "port").getInt(8080);
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
