package edu.zieit.scheduler.bot.states.course;

import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.state.InputResult;
import edu.zieit.scheduler.bot.state.State;
import edu.zieit.scheduler.persistence.entity.SubsCourse;
import edu.zieit.scheduler.schedule.course.CourseSchedule;
import edu.zieit.scheduler.services.SubsService;
import edu.zieit.scheduler.util.ChatUtil;
import edu.zieit.scheduler.util.FilenameUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StateCourse extends State {

    @Override
    public void activate(ChatSession session) {
        SubsService subsService = session.getSubsService();
        SubsCourse subs = subsService.getCourseSubs(session.getChatId());

        if (subs != null) {
            ScheduleService service = session.getScheduleService();
            Schedule schedule = service.getCourseSchedule(subs.getScheduleKey());

            if (schedule instanceof CourseSchedule course) {
                InputStream img = new ByteArrayInputStream(schedule.toImage());
                String caption = String.format(session.getLang().of("cmd.course.caption"), course.getDisplayName());

                session.reply(ChatUtil.editableMessage(session, img,
                        FilenameUtil.getNameWithExt(service, "photo"), caption));
            } else {
                session.reply(session.getLang().of("cmd.course.notfound"));
            }
        } else {
            session.reply(session.getLang().of("cmd.course.nosubs"));
        }
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
