package edu.zieit.scheduler.bot.states.aud;

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

public class StateAudShow extends State {

    @Override
    public void activate(ChatSession session) {
        ScheduleService service = session.getChatManager().getBot().getScheduleService();
        String classroom = session.getString("aud");

        if (classroom != null) {
            Optional<Schedule> opt = service.getCourseByGroup(group);

            if (opt.isPresent()) {
                ScheduleRenderer renderer = opt.get().getPersonalRenderer(group, service);
                InputStream img = new ByteArrayInputStream(renderer.renderBytes());
                String caption = String.format(session.getLang().of("cmd.aud.caption"), group);

                session.getChatManager().getBot().send(session, ChatUtil.editableMessage(session, img,
                        FilenameUtil.getNameWithExt(service, "photo"), caption));
            } else {
                session.getBot().sendMessage(session, session.getLang().of("cmd.aud.notfound"));
            }
        } else {
            session.getBot().sendMessage(session, session.getLang().of("cmd.aud.notfound"));
        }
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
