package edu.zieit.scheduler.bot.states.points;

import edu.zieit.scheduler.api.Person;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputResult;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.services.PointsService;
import edu.zieit.scheduler.util.ChatUtil;
import edu.zieit.scheduler.util.FilenameUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StatePointsShow extends State {

    @Override
    public void activate(ChatSession session) {
        Person person = session.get("person");
        String password = session.getString("password");

        if (!send(session, person, password)) {
            // If user entered invalid credentials, remove this wrong saved data
            session.getBot().getSubsService()
                    .unsubscribePoints(session.getChatId());
        }
    }

    private boolean send(ChatSession session, Person person, String password) {
        try {
            PointsService pointsService = session.getBot().getPointsService();
            byte[] bytes = pointsService.getPoints(person, password);

            if (bytes != null) {
                ScheduleService service = session.getBot().getScheduleService();
                InputStream img = new ByteArrayInputStream(bytes);
                String caption = session.getLang().of("cmd.points.caption");

                session.getBot().send(session, ChatUtil.editableMessage(session, img,
                        FilenameUtil.getNameWithExt(service, "photo"), caption));

                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        session.reply(session.getLang().of("cmd.points.error"));

        return false;
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
