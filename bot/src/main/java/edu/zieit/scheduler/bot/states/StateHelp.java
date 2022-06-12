package edu.zieit.scheduler.bot.states;

import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.state.InputResult;
import edu.zieit.scheduler.bot.state.State;
import edu.zieit.scheduler.api.config.Language;

import java.util.List;

public class StateHelp extends State {

    private final String message;

    public StateHelp(Language lang) {
        List<String> list = lang.ofList("cmd.help");
        this.message = String.join("\n", list);
    }

    @Override
    public void activate(ChatSession session) {
        session.reply(message);
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }
}
