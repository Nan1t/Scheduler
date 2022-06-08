package edu.zieit.scheduler.bot.states.group;

import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.state.InputResult;
import edu.zieit.scheduler.bot.state.State;
import edu.zieit.scheduler.persistence.entity.SubsGroup;
import edu.zieit.scheduler.services.SubsService;
import edu.zieit.scheduler.util.ChatUtil;
import edu.zieit.scheduler.util.FilenameUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

public class StateGroup extends State {

    @Override
    public void activate(ChatSession session) {
        SubsService subsService = session.getSubsService();
        SubsGroup subs = subsService.getGroupSubs(session.getChatId());

        if (subs != null) {
            ScheduleService service = session.getScheduleService();
            Optional<Schedule> schedule = service.getCourseByGroup(subs.getGroup());

            if (schedule.isPresent()) {
                ScheduleRenderer renderer = schedule.get().getPersonalRenderer(subs.getGroup(), service);
                InputStream img = new ByteArrayInputStream(renderer.renderBytes());
                String caption = String.format(session.getLang().of("cmd.group.caption"), subs.getGroup());

                session.reply(ChatUtil.editableMessage(session, img,
                        FilenameUtil.getNameWithExt(service, "photo"), caption));
            } else {
                session.reply(session.getLang().of("cmd.group.notfound"));
            }
        } else {
            session.reply(session.getLang().of("cmd.group.nosubs"));
        }
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
