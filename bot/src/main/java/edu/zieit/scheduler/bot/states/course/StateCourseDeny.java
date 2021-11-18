package edu.zieit.scheduler.bot.states.course;

import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputResult;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.services.SubsService;

public class StateCourseDeny extends State {

    @Override
    public void activate(ChatSession session) {
        SubsService subsService = session.getBot().getSubsService();
        boolean res = subsService.unsubscribeCourse(session.getChatId());

        if (res) {
            session.getBot().sendMessage(session,
                    session.getLang().of("cmd.course.deny"));
        } else {
            session.getBot().sendMessage(session,
                    session.getLang().of("cmd.course.nosubs"));
        }
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
