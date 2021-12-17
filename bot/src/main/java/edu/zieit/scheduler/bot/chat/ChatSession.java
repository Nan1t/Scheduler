package edu.zieit.scheduler.bot.chat;

import edu.zieit.scheduler.bot.SchedulerBot;
import napi.configurate.yaml.lang.Language;

import java.util.HashMap;
import java.util.Map;

public class ChatSession {

    private final ChatManager chatManager;
    private final String chatId;
    private final Map<String, Object> args = new HashMap<>();

    private State state;
    private int lastMsgId;

    public ChatSession(ChatManager chatManager, String chatId) {
        this.chatManager = chatManager;
        this.chatId = chatId;
        resetLastMsgId();
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public SchedulerBot getBot() {
        return chatManager.getBot();
    }

    public Language getLang() {
        return chatManager.getBot().getLang();
    }

    public String getChatId() {
        return chatId;
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
        getBot().sendMessage(this, message);
    }

}
