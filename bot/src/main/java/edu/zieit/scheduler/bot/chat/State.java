package edu.zieit.scheduler.bot.chat;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

public interface State {

    BotApiMethod activate(InputContext ctx);

    BotApiMethod onInput(InputContext ctx);

}
