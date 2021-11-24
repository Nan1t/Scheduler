package edu.zieit.scheduler.bot.states.consult;

import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputResult;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.services.SubsService;

public class StateConsultDeny extends State {

    @Override
    public void activate(ChatSession session) {
        SubsService subsService = session.getBot().getSubsService();
        boolean res = subsService.unsubscribeConsult(session.getChatId());

        if (res) {
            session.getBot().sendMessage(session,
                    session.getLang().of("cmd.consult.deny"));
        } else {
            session.getBot().sendMessage(session,
                    session.getLang().of("cmd.consult.nosubs"));
        }
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
