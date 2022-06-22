package edu.zieit.scheduler.bot.states.consult;

import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.state.InputResult;
import edu.zieit.scheduler.bot.state.State;
import edu.zieit.scheduler.util.ChatUtil;
import edu.zieit.scheduler.util.FilenameUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StateConsultAll extends State {

    @Override
    public void activate(ChatSession session) {
        ScheduleService service = session.getScheduleService();
        Schedule schedule = service.getConsultSchedule();
        InputStream img = new ByteArrayInputStream(schedule.toImage());
        String caption = session.getLang().of("cmd.consult.all.caption");

        session.reply(ChatUtil.editableMessage(session, img,
                FilenameUtil.getNameWithExt(service, "photo"), caption));
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
