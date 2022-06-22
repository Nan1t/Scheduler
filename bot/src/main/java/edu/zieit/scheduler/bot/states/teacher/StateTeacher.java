package edu.zieit.scheduler.bot.states.teacher;

import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.state.InputResult;
import edu.zieit.scheduler.bot.state.State;
import edu.zieit.scheduler.persistence.entity.SubsTeacher;
import edu.zieit.scheduler.services.SubsService;
import edu.zieit.scheduler.util.ChatUtil;
import edu.zieit.scheduler.util.FilenameUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StateTeacher extends State {

    @Override
    public void activate(ChatSession session) {
        SubsService subsService = session.getSubsService();
        SubsTeacher subs = subsService.getTeacherSubs(session.getChatId());

        if (subs != null) {
            ScheduleService service = session.getScheduleService();
            ScheduleRenderer renderer = service.getTeacherSchedule().getPersonalRenderer(subs.getTeacher(), service);
            InputStream img = new ByteArrayInputStream(renderer.renderBytes());
            String caption = String.format(session.getLang().of("cmd.teacher.caption"), subs.getTeacher());

            session.reply(ChatUtil.editableMessage(session,
                    img,
                    FilenameUtil.getNameWithExt(service, "photo"),
                    caption));
        } else {
            session.reply(session.getLang().of("cmd.teacher.nosubs"));
        }
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
