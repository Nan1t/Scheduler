package edu.zieit.scheduler.bot.states.teacher;

import edu.zieit.scheduler.api.Person;
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

public class StateTeacherShow extends State {

    private final boolean saveSubs;

    public StateTeacherShow(boolean saveSubs) {
        this.saveSubs = saveSubs;
    }

    @Override
    public void activate(ChatSession session) {
        Person person = Person.teacher(session.getString("teacher"));
        
        if (person != null) {
            ScheduleService service = session.getScheduleService();
            ScheduleRenderer renderer = service.getTeacherSchedule().getPersonalRenderer(person, service);
            InputStream img = new ByteArrayInputStream(renderer.renderBytes());
            String caption = saveSubs
                    ? String.format(session.getLang().of("cmd.teacher.subscribed"), person)
                    : String.format(session.getLang().of("cmd.teacher.caption"), person);

            session.reply(ChatUtil.editableMessage(session, img,
                    FilenameUtil.getNameWithExt(service, "photo"), caption));

            if (saveSubs) {
                session.getSubsService()
                        .subscribeTeacher(session.getChatId(), person);
            }
        }
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
