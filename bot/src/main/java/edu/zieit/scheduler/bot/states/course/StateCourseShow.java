package edu.zieit.scheduler.bot.states.course;

import edu.zieit.scheduler.api.NamespacedKey;

import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputResult;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.schedule.course.CourseSchedule;
import edu.zieit.scheduler.util.ChatUtil;
import edu.zieit.scheduler.util.FilenameUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StateCourseShow extends State {

    private final boolean saveSubs;

    public StateCourseShow(boolean saveSubs) {
        this.saveSubs = saveSubs;
    }

    @Override
    public void activate(ChatSession session) {
        ScheduleService service = session.getChatManager().getBot().getScheduleService();
        NamespacedKey key = NamespacedKey.parse(session.getString("course"));
        Schedule schedule = service.getCourseSchedule(key);

        if (schedule instanceof CourseSchedule stud) {
            InputStream img = new ByteArrayInputStream(schedule.toImage());
            String caption = saveSubs
                    ? String.format(session.getLang().of("cmd.course.subscribed"), stud.getDisplayName())
                    : String.format(session.getLang().of("cmd.course.caption"), stud.getDisplayName());

            session.getChatManager().getBot().send(session, ChatUtil.editableMessage(session, img,
                    FilenameUtil.getNameWithExt(service, "photo"), caption));

            if (saveSubs) {
                session.getBot().getSubsService()
                        .subscribeStudent(session.getChatId(), key);
            }
        }
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
