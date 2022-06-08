package edu.zieit.scheduler.bot.state;

import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;

public abstract class State {

    private final State next;

    public State(State next) {
        this.next = next;
    }

    public State() {
        this(null);
    }

    public State getNext() {
        return next;
    }

    public boolean hasNext() {
        return next != null;
    }

    public abstract void activate(ChatSession session);

    public abstract InputResult input(ChatInput input, ChatSession session);

}
