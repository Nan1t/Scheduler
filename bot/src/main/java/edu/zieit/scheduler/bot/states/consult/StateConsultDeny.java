package edu.zieit.scheduler.bot.states.consult;

import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.state.InputResult;
import edu.zieit.scheduler.bot.state.State;
import edu.zieit.scheduler.services.SubsService;

public class StateConsultDeny extends State {

    @Override
    public void activate(ChatSession session) {
        SubsService subsService = session.getSubsService();
        boolean res = subsService.unsubscribeConsult(session.getUser());

        if (res) {
            session.reply(session.getLang().of("cmd.consult.deny"));
        } else {
            session.reply(session.getLang().of("cmd.consult.nosubs"));
        }
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
