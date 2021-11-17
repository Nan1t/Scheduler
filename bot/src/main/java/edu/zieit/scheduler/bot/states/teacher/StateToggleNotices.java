package edu.zieit.scheduler.bot.states.teacher;

import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputResult;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.services.SubsService;

public class StateToggleNotices extends State {

    @Override
    public void activate(ChatSession session) {
        SubsService subsService = session.getBot().getSubsService();
        boolean res = subsService.toggleNotices(session.getChatId());

        if (res) {
            session.getBot().sendMessage(session,
                    session.getLang().of("cmd.teacher.notice.on"));
        } else {
            session.getBot().sendMessage(session,
                    session.getLang().of("cmd.teacher.notice.off"));
        }
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
