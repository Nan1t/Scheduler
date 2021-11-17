package edu.zieit.scheduler.bot.states.students;

import edu.zieit.scheduler.api.NamespacedKey;

import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputResult;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.util.ChatUtil;
import edu.zieit.scheduler.util.FilenameUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StateStudentShow extends State {

    @Override
    public void activate(ChatSession session) {
        ScheduleService service = session.getChatManager().getBot().getScheduleService();
        NamespacedKey key = NamespacedKey.parse(session.getString("course"));
        Schedule schedule = service.getStudentSchedule(key);

        if (schedule != null) {
            InputStream img = new ByteArrayInputStream(schedule.toImage());
            String caption = session.getLang().of("cmd.teacher.caption");

            session.getChatManager().getBot().send(session, ChatUtil.editableMessage(session, img,
                    FilenameUtil.getNameWithExt(service, "photo"), caption));
        }
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
