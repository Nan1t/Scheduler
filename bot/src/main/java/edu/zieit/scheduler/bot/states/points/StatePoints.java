package edu.zieit.scheduler.bot.states.points;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.state.InputResult;
import edu.zieit.scheduler.bot.state.State;
import edu.zieit.scheduler.persistence.entity.SubsPoint;
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
        SubsService subsService = session.getSubsService();

        if (session.has("person") && session.has("password")) {
            Person person = session.get("person");
            String password = session.getString("password");

            send(session, person, password);
        } else {
            SubsPoint subs = subsService.getPointsSubs(session.getChatId());

            if (subs != null) {
                send(session, subs.getPerson(), subs.getPassword());
            } else {
                session.updateState(getNext());
            }
        }
    }

    private void send(ChatSession session, Person person, String password) {
        try {
            PointsService pointsService = session.getPointsService();
            byte[] bytes = pointsService.getPoints(person, password);

            if (bytes != null) {
                ScheduleService service = session.getScheduleService();
                InputStream img = new ByteArrayInputStream(bytes);
                String caption = session.getLang().of("cmd.points.caption");

                session.reply(ChatUtil.editableMessage(
                        session,
                        img,
                        FilenameUtil.getNameWithExt(service, "photo"),
                        caption));

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
