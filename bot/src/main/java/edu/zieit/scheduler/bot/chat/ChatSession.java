package edu.zieit.scheduler.bot.chat;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.Bot;
import edu.zieit.scheduler.bot.state.State;
import edu.zieit.scheduler.persistence.entity.BotUser;
import edu.zieit.scheduler.services.PointsService;
import edu.zieit.scheduler.services.SubsService;
import napi.configurate.yaml.lang.Language;

import java.util.HashMap;
import java.util.Map;

public class ChatSession {

    private final Bot bot;
    private final Language lang;
    private final ScheduleService scheduleService;
    private final SubsService subsService;
    private final PointsService pointsService;

    private final BotUser user;
    private final String chatId;
    private final Map<String, Object> args = new HashMap<>();

    private State state;
    private int lastMsgId;

    @AssistedInject
    public ChatSession(
            Bot bot,
            Language lang,
            ScheduleService scheduleService,
            SubsService subsService,
            PointsService pointsService,
            @Assisted String chatId,
            @Assisted BotUser user
    ) {
        this.bot = bot;
        this.lang = lang;
        this.scheduleService = scheduleService;
        this.subsService = subsService;
        this.pointsService = pointsService;
        this.chatId = chatId;
        this.user = user;
        resetLastMsgId();
    }

    public Bot getBot() {
        return bot;
    }

    public Language getLang() {
        return lang;
    }

    public ScheduleService getScheduleService() {
        return scheduleService;
    }

    public SubsService getSubsService() {
        return subsService;
    }

    public PointsService getPointsService() {
        return pointsService;
    }

    public String getChatId() {
        return chatId;
    }

    public BotUser getUser() {
        return user;
    }

    public State getState() {
        return state;
    }

    public void updateState(State state) {
        this.state = state.hasNext() ? state : null;
        state.activate(this);
    }

    public int getLastMsgId() {
        return lastMsgId;
    }

    public void setLastMsgId(int lastMsgId) {
        this.lastMsgId = lastMsgId;
    }

    public void resetLastMsgId() {
        setLastMsgId(Integer.MIN_VALUE);
    }

    public boolean hasValidLastMsgId() {
        return lastMsgId != Integer.MIN_VALUE;
    }

    public <T> T get(String key) {
        return get(key, null);
    }

    public <T> T get(String key, T def) {
        return (T) getRaw(key, def);
    }

    public boolean has(String key) {
        return args.containsKey(key);
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String def) {
        return getRaw(key, def).toString();
    }

    public Object getRaw(String key, Object def) {
        return args.getOrDefault(key, def);
    }

    public Object getRaw(String key) {
        return getRaw(key, null);
    }

    public void add(String key, Object value) {
        args.put(key, value);
    }

    public void reply(String message) {
        bot.sendMessage(this, message);
    }

    public void reply(Object method) {
        bot.send(this, method);
    }

}
