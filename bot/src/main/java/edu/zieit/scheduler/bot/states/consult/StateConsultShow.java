package edu.zieit.scheduler.bot.states.consult;

import edu.zieit.scheduler.api.Person;
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

public class StateConsultShow extends State {

    @Override
    public void activate(ChatSession session) {
        Person person = Person.teacher(session.getString("teacher"));
        
        if (person != null) {
            ScheduleService service = session.getChatManager().getBot().getScheduleService();
            ScheduleRenderer renderer = service.getConsultSchedule().getPersonalRenderer(person, service);
            InputStream img = new ByteArrayInputStream(renderer.renderBytes());
            String caption = String.format(session.getLang().of("cmd.consult.caption"), person);

            session.getChatManager().getBot().send(session, ChatUtil.editableMessage(session, img,
                    FilenameUtil.getNameWithExt(service, "photo"), caption));
        }
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
