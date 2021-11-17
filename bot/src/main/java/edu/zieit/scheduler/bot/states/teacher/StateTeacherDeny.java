package edu.zieit.scheduler.bot.states.teacher;

import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputResult;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.services.SubsService;

public class StateTeacherDeny extends State {

    @Override
    public void activate(ChatSession session) {
        SubsService subsService = session.getBot().getSubsService();
        subsService.unsubscribeTeacher(session.getChatId());
        session.getBot().sendMessage(session, "Unsubscribed!");
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
