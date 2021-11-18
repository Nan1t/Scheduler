package edu.zieit.scheduler.bot.states.group;

import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputResult;
import edu.zieit.scheduler.bot.chat.State;
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
        ScheduleService service = session.getChatManager().getBot().getScheduleService();
        String group = session.getString("group");

        if (group != null) {
            Optional<Schedule> opt = service.getCourseByGroup(group);

            if (opt.isPresent()) {
                ScheduleRenderer renderer = opt.get().getPersonalRenderer(group, service);
                InputStream img = new ByteArrayInputStream(renderer.renderBytes());

                session.getChatManager().getBot().send(session, ChatUtil.editableMessage(session, img,
                        FilenameUtil.getNameWithExt(service, "photo"), "Group schedule photo"));
            } else {
                session.getBot().sendMessage(session, "Group not found");
            }
        } else {
            session.getBot().sendMessage(session, "Group null");
        }
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
