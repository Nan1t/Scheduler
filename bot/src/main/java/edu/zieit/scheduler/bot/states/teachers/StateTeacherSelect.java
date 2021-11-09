package edu.zieit.scheduler.bot.states.teachers;

import edu.zieit.scheduler.bot.chat.InputContext;
import edu.zieit.scheduler.bot.chat.State;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class StateTeacherSelect implements State {

    @Override
    public BotApiMethod activate(InputContext ctx) {
        return SendMessage.builder()
                .chatId(ctx.getChatId())
                .text("Hello!")
                .build();
    }

    @Override
    public BotApiMethod onInput(InputContext ctx) {
        if (ctx.getUpdate().hasMessage()) {
            return SendMessage.builder()
                    .chatId(ctx.getChatId())
                    .text("Command: '"+ctx.getUpdate().getMessage().getText()+"'")
                    .build();
        } else {
            return SendMessage.builder()
                    .chatId(ctx.getChatId())
                    .text("Undefined argument")
                    .build();
        }
    }
}
