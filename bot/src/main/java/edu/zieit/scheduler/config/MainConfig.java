package edu.zieit.scheduler.config;

import napi.configurate.yaml.conf.Configuration;
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

    private int threadPoolSize;

    public MainConfig(Path rootDir) {
        super(rootDir, "/config.yml", Map.of());
    }

    @Override
    protected void load() {
        dbProperties = loadProperties(conf.getNode("database"));
        tgBotName = conf.getNode("telegram", "bot_name").getString();
        tgToken = conf.getNode("telegram", "token").getString();

        regexTeacherDefault = Pattern.compile(conf.getNode("expressions", "teacher_default").getString(""));
        regexTeacherInline = Pattern.compile(conf.getNode("expressions", "teacher_inline").getString(""));

        threadPoolSize = conf.getNode("thread_pool_size").getInt(4);
    }

    public Configuration getConf() {
        return conf;
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

    public int getThreadPoolSize() {
        return threadPoolSize;
    }
}
