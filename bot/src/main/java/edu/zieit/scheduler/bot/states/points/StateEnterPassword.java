package edu.zieit.scheduler.bot.states.points;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.state.InputResult;
import edu.zieit.scheduler.bot.state.State;
import edu.zieit.scheduler.services.SubsService;

public class StateEnterPassword extends State {

    public StateEnterPassword() {
        super(new StatePointsShow());
    }

    @Override
    public void activate(ChatSession session) {
        session.reply(session.getLang().of("cmd.points.login.password"));
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        if (input.update().hasMessage() && input.update().getMessage().hasText()) {
            SubsService subsService = session.getSubsService();

            String password = input.update().getMessage().getText();
            Person person = session.get("person");

            session.add("password", password);

            subsService.subscribePoints(session.getChatId(), person, password);

            return InputResult.NEXT;
        }

        return InputResult.WRONG;
    }

}
