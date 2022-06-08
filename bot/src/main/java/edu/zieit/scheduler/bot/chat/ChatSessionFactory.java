package edu.zieit.scheduler.bot.chat;

import edu.zieit.scheduler.persistence.entity.BotUser;

public interface ChatSessionFactory {

    ChatSession createSession(String chatId, BotUser user);

}
