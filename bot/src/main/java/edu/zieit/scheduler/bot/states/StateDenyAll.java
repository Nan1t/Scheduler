package edu.zieit.scheduler.bot.states;

import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.state.InputResult;
import edu.zieit.scheduler.bot.state.State;
import edu.zieit.scheduler.services.SubsService;

public class StateDenyAll extends State {

    @Override
    public void activate(ChatSession session) {
        SubsService service = session.getSubsService();

        service.unsubscribeTeacher(session.getChatId());
        service.unsubscribeConsult(session.getChatId());
        service.unsubscribeCourse(session.getChatId());
        service.unsubscribeGroup(session.getChatId());

        session.reply(session.getLang().of("cmd.denyall"));
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }
}
