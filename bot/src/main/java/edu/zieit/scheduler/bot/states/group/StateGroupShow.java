package edu.zieit.scheduler.bot.states.group;

import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.state.InputResult;
import edu.zieit.scheduler.bot.state.State;
import edu.zieit.scheduler.util.ChatUtil;
import edu.zieit.scheduler.util.FilenameUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

public class StateGroupShow extends State {

    private final boolean saveSubs;

    public StateGroupShow(boolean saveSubs) {
        this.saveSubs = saveSubs;
    }

    @Override
    public void activate(ChatSession session) {
        ScheduleService service = session.getScheduleService();
        String group = session.getString("group");

        if (group != null) {
            Optional<Schedule> opt = service.getCourseByGroup(group);

            if (opt.isPresent()) {
                ScheduleRenderer renderer = opt.get().getPersonalRenderer(group, service);
                InputStream img = new ByteArrayInputStream(renderer.renderBytes());
                String caption = saveSubs
                        ? String.format(session.getLang().of("cmd.group.subscribed"), group)
                        : String.format(session.getLang().of("cmd.group.caption"), group);

                session.reply(ChatUtil.editableMessage(session, img,
                        FilenameUtil.getNameWithExt(service, "photo"), caption));

                if (saveSubs) {
                    session.getSubsService().subscribeGroup(session.getUser(), group);
                }
            } else {
                session.reply(session.getLang().of("cmd.group.notfound"));
            }
        } else {
            session.reply(session.getLang().of("cmd.group.notfound"));
        }
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
