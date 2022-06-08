package edu.zieit.scheduler.bot.states.points;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.state.InputResult;
import edu.zieit.scheduler.bot.state.State;

public class StateEnterName extends State {

    public StateEnterName() {
        super(new StateEnterPassword());
    }

    @Override
    public void activate(ChatSession session) {
        session.reply(session.getLang().of("cmd.points.login.name"));
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        if (input.update().hasMessage() && input.update().getMessage().hasText()) {
            String raw = input.update().getMessage().getText();
            String[] arr = raw.split(" ");

            if (arr.length == 3) {
                String lastName = arr[0];
                String firstName = arr[1];
                String patronymic = arr[2];

                session.add("person", Person.simple(firstName, lastName, patronymic));

                return InputResult.NEXT;
            }

            session.reply(session.getLang().of("cmd.points.login.name.wrong"));

            return InputResult.STAY;
        }

        return InputResult.WRONG;
    }

}
