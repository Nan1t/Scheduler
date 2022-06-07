package edu.zieit.scheduler.bot.states.consult;

import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputResult;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.services.SubsService;
import edu.zieit.scheduler.util.ChatUtil;
import edu.zieit.scheduler.util.FilenameUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StateConsult extends State {

    @Override
    public void activate(ChatSession session) {
        SubsService subsService = session.getBot().getSubsService();
        SubscriptionConsult subs = subsService.getConsultSubs(session.getChatId());

        if (subs != null) {
            ScheduleService service = session.getChatManager().getBot().getScheduleService();
            ScheduleRenderer renderer = service.getConsultSchedule().getPersonalRenderer(subs.getTeacher(), service);
            InputStream img = new ByteArrayInputStream(renderer.renderBytes());
            String caption = String.format(session.getLang().of("cmd.consult.caption"), subs.getTeacher());

            session.getChatManager().getBot().send(session, ChatUtil.editableMessage(session, img,
                    FilenameUtil.getNameWithExt(service, "photo"), caption));
        } else {
            session.getBot().sendMessage(session, session.getLang().of("cmd.consult.nosubs"));
        }
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
