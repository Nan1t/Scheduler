package edu.zieit.scheduler.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatManager;
import edu.zieit.scheduler.services.*;

public class ServicesModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SubsService.class).in(Scopes.SINGLETON);
        bind(ScheduleService.class)
                .to(ScheduleServiceImpl.class)
                .in(Scopes.SINGLETON);
        bind(PointsService.class).in(Scopes.SINGLETON);
        bind(TimerService.class).in(Scopes.SINGLETON);
        bind(ChatManager.class).in(Scopes.SINGLETON);
    }

}
