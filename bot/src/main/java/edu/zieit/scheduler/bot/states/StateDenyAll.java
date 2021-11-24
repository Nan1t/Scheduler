package edu.zieit.scheduler.bot.states;

import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputResult;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.services.SubsService;

public class StateDenyAll extends State {

    @Override
    public void activate(ChatSession session) {
        SubsService service = session.getBot().getSubsService();

        service.unsubscribeTeacher(session.getChatId());
        service.unsubscribeConsult(session.getChatId());
        service.unsubscribeCourse(session.getChatId());
        service.unsubscribeGroup(session.getChatId());

        session.getBot().sendMessage(session, session.getLang().of("cmd.denyall"));
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }
}
