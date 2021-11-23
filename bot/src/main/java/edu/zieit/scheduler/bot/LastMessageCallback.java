package edu.zieit.scheduler.bot;

import edu.zieit.scheduler.bot.chat.ChatManager;
import edu.zieit.scheduler.bot.chat.ChatSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

public record LastMessageCallback(ChatSession session)
        implements SentCallback<Message> {

    private static final Logger logger = LogManager.getLogger(ChatManager.class);

    @Override
    public void onResult(BotApiMethod<Message> botApiMethod, Message message) {
        if (session != null)
            session.setLastMsgId(message.getMessageId());
    }

    @Override
    public void onError(BotApiMethod<Message> botApiMethod, TelegramApiRequestException e) {
        logger.error("Error while sending message: ", e);
    }

    @Override
    public void onException(BotApiMethod<Message> botApiMethod, Exception e) {
        logger.error("Error while sending message: ", e);
    }

}
