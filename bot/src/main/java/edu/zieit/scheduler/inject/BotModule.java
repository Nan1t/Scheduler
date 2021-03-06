package edu.zieit.scheduler.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import edu.zieit.scheduler.bot.Bot;
import edu.zieit.scheduler.bot.SchedulerBot;
import edu.zieit.scheduler.bot.chat.ChatManager;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.ChatSessionFactory;
import edu.zieit.scheduler.bot.state.StateRegistry;

public class BotModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Bot.class)
                .to(SchedulerBot.class)
                .in(Scopes.SINGLETON);

        install(new FactoryModuleBuilder()
                .implement(ChatSession.class, ChatSession.class)
                .build(ChatSessionFactory.class));

        bind(ChatManager.class).in(Scopes.SINGLETON);
        bind(StateRegistry.class).in(Scopes.SINGLETON);
    }

}
