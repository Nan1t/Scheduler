package edu.zieit.scheduler.bot.states.course;

import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputResult;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.persistence.subscription.SubscriptionCourse;
import edu.zieit.scheduler.schedule.course.CourseSchedule;
import edu.zieit.scheduler.services.SubsService;
import edu.zieit.scheduler.util.ChatUtil;
import edu.zieit.scheduler.util.FilenameUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StateCourse extends State {

    @Override
    public void activate(ChatSession session) {
        SubsService subsService = session.getBot().getSubsService();
        SubscriptionCourse subs = subsService.getCourseSubs(session.getChatId());

        if (subs != null) {
            ScheduleService service = session.getChatManager().getBot().getScheduleService();
            Schedule schedule = service.getCourseSchedule(subs.getScheduleKey());

            if (schedule instanceof CourseSchedule course) {
                InputStream img = new ByteArrayInputStream(schedule.toImage());
                String caption = String.format(session.getLang().of("cmd.course.caption"), course.getDisplayName());

                session.getChatManager().getBot().send(session, ChatUtil.editableMessage(session, img,
                        FilenameUtil.getNameWithExt(service, "photo"), caption));
            } else {
                session.getBot().sendMessage(session, session.getLang().of("cmd.course.notfound"));
            }
        } else {
            session.getBot().sendMessage(session, session.getLang().of("cmd.course.nosubs"));
        }
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}