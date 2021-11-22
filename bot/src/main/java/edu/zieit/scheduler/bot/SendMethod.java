package edu.zieit.scheduler.bot;

import edu.zieit.scheduler.bot.chat.ChatSession;

public record SendMethod(ChatSession session, Object method) {

}
