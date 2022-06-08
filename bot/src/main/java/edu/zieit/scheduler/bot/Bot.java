package edu.zieit.scheduler.bot;

import edu.zieit.scheduler.bot.chat.ChatSession;

public interface Bot {

    String getUsername();

    void send(ChatSession session, Object... methods);

    void send(Object method);

    void sendMessage(ChatSession session, String msg);

}
