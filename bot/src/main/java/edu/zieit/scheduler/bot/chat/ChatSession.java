package edu.zieit.scheduler.bot.chat;

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
        setLastMsgId(Integer.MIN_VALUE);
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public String getChatId() {
        return chatId;
    }

    public State getState() {
        return state;
    }

    public void updateState(State state) {
        this.state = state;
        state.activate(this);
    }

    public int getLastMsgId() {
        return lastMsgId;
    }

    public void setLastMsgId(int lastMsgId) {
        this.lastMsgId = lastMsgId;
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

}
