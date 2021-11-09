package edu.zieit.scheduler.bot.chat;

import edu.zieit.scheduler.bot.ChatManager;
import org.telegram.telegrambots.meta.api.objects.Update;

public class InputContext {

    private final ChatSession session;
    private final ChatManager manager;
    private final Update update;
    private final String chatId;

    public InputContext(ChatSession session, ChatManager manager, Update update) {
        this.session = session;
        this.manager = manager;
        this.update = update;
        this.chatId = update.getMessage().getChatId().toString();
    }

    public ChatSession getSession() {
        return session;
    }

    public ChatManager getManager() {
        return manager;
    }

    public Update getUpdate() {
        return update;
    }

    public String getChatId() {
        return chatId;
    }
}
