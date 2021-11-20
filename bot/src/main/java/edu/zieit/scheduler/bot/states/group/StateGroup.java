package edu.zieit.scheduler.bot.states.group;

import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputResult;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.persistence.subscription.SubscriptionGroup;
import edu.zieit.scheduler.services.SubsService;
import edu.zieit.scheduler.util.ChatUtil;
import edu.zieit.scheduler.util.FilenameUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

public class StateGroup extends State {

    @Override
    public void activate(ChatSession session) {
        SubsService subsService = session.getBot().getSubsService();
        SubscriptionGroup subs = subsService.getGroupSubs(session.getChatId());

        if (subs != null) {
            ScheduleService service = session.getChatManager().getBot().getScheduleService();
            Optional<Schedule> schedule = service.getCourseByGroup(subs.getGroupName());

            if (schedule.isPresent()) {
                ScheduleRenderer renderer = schedule.get().getPersonalRenderer(subs.getGroupName(), service);
                InputStream img = new ByteArrayInputStream(renderer.renderBytes());
                String caption = String.format(session.getLang().of("cmd.group.caption"), subs.getGroupName());

                session.getChatManager().getBot().send(session, ChatUtil.editableMessage(session, img,
                        FilenameUtil.getNameWithExt(service, "photo"), caption));
            } else {
                session.getBot().sendMessage(session, session.getLang().of("cmd.group.notfound"));
            }
        } else {
            session.getBot().sendMessage(session, session.getLang().of("cmd.group.nosubs"));
        }
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
