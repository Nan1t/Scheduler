package edu.zieit.scheduler.bot.states.consult;

import edu.zieit.scheduler.api.schedule.ScheduleRenderer;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputResult;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.persistence.subscription.SubscriptionConsult;
import edu.zieit.scheduler.services.SubsService;
import edu.zieit.scheduler.util.ChatUtil;
import edu.zieit.scheduler.util.FilenameUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StateConsult extends State {

    @Override
    public void activate(ChatSession session) {
        System.out.println("Called consult");

        SubsService subsService = session.getBot().getSubsService();
        SubscriptionConsult subs = subsService.getConsultSubs(session.getChatId());

        System.out.println("1");

        if (subs != null) {
            System.out.println("2");
            ScheduleService service = session.getChatManager().getBot().getScheduleService();
            System.out.println("3");
            ScheduleRenderer renderer = service.getConsultSchedule().getPersonalRenderer(subs.getTeacher(), service);
            System.out.println("4");
            InputStream img = new ByteArrayInputStream(renderer.renderBytes());
            System.out.println("5");
            String caption = String.format(session.getLang().of("cmd.consult.caption"), subs.getTeacher());
            System.out.println("6");

            session.getChatManager().getBot().send(session, ChatUtil.editableMessage(session, img,
                    FilenameUtil.getNameWithExt(service, "photo"), caption));
            System.out.println("7");
        } else {
            System.out.println("8");
            session.getBot().sendMessage(session, session.getLang().of("cmd.consult.nosubs"));
            System.out.println("9");
        }
        System.out.println("10");
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
