package edu.zieit.scheduler.bot.chat;

import org.telegram.telegrambots.meta.api.objects.Update;

public class ChatInput {

    private final ChatSession session;
    private final Update update;
    private final String chatId;

    public ChatInput(String chatId, ChatSession session, Update update) {
        this.session = session;
        this.update = update;
        this.chatId = chatId;
    }

    public ChatSession getSession() {
        return session;
    }

    public Update getUpdate() {
        return update;
    }

    public String getChatId() {
        return chatId;
    }

    public int getMessageId() {
        if (update.hasMessage())
            return update.getMessage().getMessageId();
        if (update.hasCallbackQuery())
            return update.getCallbackQuery().getMessage().getMessageId();
        return Integer.MIN_VALUE;
    }
}
