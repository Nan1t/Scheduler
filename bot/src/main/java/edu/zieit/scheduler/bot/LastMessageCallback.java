package edu.zieit.scheduler.bot;

import edu.zieit.scheduler.bot.chat.ChatSession;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

public record LastMessageCallback(ChatSession session)
        implements SentCallback<Message> {

    @Override
    public void onResult(BotApiMethod<Message> botApiMethod, Message message) {
        session.setLastMsgId(message.getMessageId());
    }

    @Override
    public void onError(BotApiMethod<Message> botApiMethod, TelegramApiRequestException e) {
        // Ignore
    }

    @Override
    public void onException(BotApiMethod<Message> botApiMethod, Exception e) {
        // Ignore
    }

}
