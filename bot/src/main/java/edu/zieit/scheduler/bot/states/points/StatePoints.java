package edu.zieit.scheduler.bot.states.points;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputResult;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.persistence.subscription.SubscriptionPoints;
import edu.zieit.scheduler.services.PointsService;
import edu.zieit.scheduler.services.SubsService;
import edu.zieit.scheduler.util.ChatUtil;
import edu.zieit.scheduler.util.FilenameUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StatePoints extends State {

    public StatePoints() {
        super(new StateEnterName());
    }

    @Override
    public void activate(ChatSession session) {
        SubsService subsService = session.getBot().getSubsService();

        if (session.has("person") && session.has("password")) {
            Person person = session.get("person");
            String password = session.getString("password");

            send(session, person, password);
        } else {
            SubscriptionPoints subs = subsService.getPointsSubs(session.getChatId());

            if (subs != null) {
                send(session, subs.getPerson(), subs.getPassword());
            } else {
                session.updateState(getNext());
            }
        }
    }

    private void send(ChatSession session, Person person, String password) {
        try {
            PointsService pointsService = session.getBot().getPointsService();
            byte[] bytes = pointsService.getPoints(person, password);

            if (bytes != null) {
                ScheduleService service = session.getBot().getScheduleService();
                InputStream img = new ByteArrayInputStream(bytes);
                String caption = session.getLang().of("cmd.points.caption");

                session.getBot().send(session, ChatUtil.editableMessage(session, img,
                        FilenameUtil.getNameWithExt(service, "photo"), caption));

                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        session.reply(session.getLang().of("cmd.points.error"));
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
