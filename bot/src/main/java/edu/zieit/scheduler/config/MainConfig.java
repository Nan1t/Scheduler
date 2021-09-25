package edu.zieit.scheduler.config;

import napi.configurate.yaml.conf.Configuration;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Pattern;

public final class MainConfig extends AbstractConfig {

    private String dbHost;
    private int dbPort;
    private String dbUser;
    private String dbPassword;
    private Map<String, String> dbProperties;

    private String tgBotName;
    private String tgToken;

    private Pattern regexTeacherDefault;
    private Pattern regexTeacherInline;

    public MainConfig(Path rootDir) {
        super(rootDir, "/config.yml", Map.of());
    }

    @Override
    protected void load() {
        dbHost = conf.getNode("database", "host").getString();
        dbPort = conf.getNode("database", "port").getInt();
        dbUser = conf.getNode("database", "user").getString();
        dbPassword = conf.getNode("database", "password").getString();
        dbProperties = loadProperties(conf.getNode("database", "properties"));

        tgBotName = conf.getNode("telegram", "bot_name").getString();
        tgToken = conf.getNode("telegram", "token").getString();

        regexTeacherDefault = Pattern.compile(conf.getNode("expressions", "teacher_default").getString(""));
        regexTeacherInline = Pattern.compile(conf.getNode("expressions", "teacher_inline").getString(""));
    }

    public Configuration getConf() {
        return conf;
    }

    public String getDbHost() {
        return dbHost;
    }

    public int getDbPort() {
        return dbPort;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public Map<String, String> getDbProperties() {
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
}
