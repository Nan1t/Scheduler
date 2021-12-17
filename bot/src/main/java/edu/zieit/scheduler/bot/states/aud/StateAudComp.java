package edu.zieit.scheduler.bot.states.aud;

import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputResult;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.schedule.classroom.SplitRenderer;
import edu.zieit.scheduler.util.ChatUtil;
import edu.zieit.scheduler.util.FilenameUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

public class StateAudComp extends State {

    @Override
    public void activate(ChatSession session) {
        ScheduleService service = session.getChatManager().getBot().getScheduleService();

        byte[] bytes = new SplitRenderer(Arrays.asList(
                "ауд.007",
                "ауд.009",
                "ауд.203",
                "ауд.204"
        ), service).renderBytes();
        InputStream img = new ByteArrayInputStream(bytes);

        session.getChatManager().getBot().send(session, ChatUtil.editableMessage(session, img,
                FilenameUtil.getNameWithExt(service, "photo"), "test"));
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        return InputResult.WRONG;
    }

}
