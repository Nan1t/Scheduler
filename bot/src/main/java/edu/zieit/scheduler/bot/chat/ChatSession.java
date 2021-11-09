package edu.zieit.scheduler.bot.chat;

import com.google.common.base.Preconditions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public class ChatSession {

    private final String chatId;
    private State currentState;

    public ChatSession(String chatId) {
        this.chatId = chatId;
    }

    public String getChatId() {
        return chatId;
    }

    public State getCurrentState() {
        return currentState;
    }

    public BotApiMethod onInput(InputContext ctx) {
        return currentState != null ? currentState.onInput(ctx) : null;
    }

    public BotApiMethod updateState(InputContext ctx, State state) {
        Preconditions.checkNotNull(state, "Chat state cannot be null");
        currentState = state;
        return state.activate(ctx);
    }

}
