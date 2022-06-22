package edu.zieit.scheduler.bot.states.points;

import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.state.InputResult;
import edu.zieit.scheduler.bot.state.State;
import edu.zieit.scheduler.services.SubsService;

public class StatePointsDeny extends State {

    @Override
    public void activate(ChatSession session) {
        SubsService subsService = session.getSubsService();
        boolean res = subsService.unsubscribePoints(session.getUser());

        if (res) {
            session.reply(session.getLang().of("cmd.points.logout.success"));
        } else {
            session.reply(session.getLang().of("cmd.points.logout.nologin"));
        }
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
