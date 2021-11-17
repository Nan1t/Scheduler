package edu.zieit.scheduler.bot.states.teacher;

import edu.zieit.scheduler.api.Pair;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputResult;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.bot.states.ChoiceState;
import edu.zieit.scheduler.schedule.teacher.TeacherSchedule;
import edu.zieit.scheduler.util.ChatUtil;
import napi.configurate.yaml.lang.Language;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.List;
import java.util.stream.Collectors;

public class StateTeacherList extends ChoiceState {

    private final Language lang;
    private final String prevPageText;
    private final String nextPageText;

    public StateTeacherList(Language lang, State nextState) {
        super(nextState);
        this.lang = lang;
        prevPageText = lang.of("choice.prev");
        nextPageText = lang.of("choice.next");
    }

    @Override
    protected int elemOnPage() {
        return 16;
    }

    @Override
    protected String prevPageText() {
        return prevPageText;
    }

    @Override
    protected String nextPageText() {
        return nextPageText;
    }

    @Override
    public void activate(ChatSession session) {
        session.getChatManager()
                .getBot()
                .send(session, buildListMessage(session, 0));
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        if (input.getUpdate().hasCallbackQuery()) {
            int page = getPageCmd(input.getUpdate());

            if (page != -1) {
                session.getChatManager()
                        .getBot()
                        .send(session, buildListMessage(session, page));
                return InputResult.STAY;
            }

            session.add("teacher", input.getUpdate()
                    .getCallbackQuery().getData());

            return InputResult.NEXT;
        }
        return InputResult.WRONG;
    }

    private BotApiMethod buildListMessage(ChatSession session, int page) {
        ScheduleService service = session.getChatManager().getBot().getScheduleService();
        List<Pair<String, String>> teachers = getTeachersList(service);
        return ChatUtil.editableMessage(session, buildKeyboard(page, teachers),
                lang.of("cmd.teacher.subs.list"));
    }

    private List<Pair<String, String>> getTeachersList(ScheduleService service) {
        var schedule = (TeacherSchedule) service.getTeacherSchedule();
        return schedule.getTeachers().stream()
                .map(str -> Pair.of(str, str))
                .collect(Collectors.toList());
    }
}
