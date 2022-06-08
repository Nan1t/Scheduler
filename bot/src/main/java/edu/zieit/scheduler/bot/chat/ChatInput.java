package edu.zieit.scheduler.bot.chat;

import org.telegram.telegrambots.meta.api.objects.Update;

public record ChatInput(String chatId, ChatSession session, Update update) {

    public int getMessageId() {
        if (update.hasMessage())
            return update.getMessage().getMessageId();
        if (update.hasCallbackQuery())
            return update.getCallbackQuery().getMessage().getMessageId();
        return Integer.MIN_VALUE;
    }
}
