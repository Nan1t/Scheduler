package edu.zieit.scheduler.bot.states.course;

import edu.zieit.scheduler.api.Pair;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputResult;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.bot.states.ChoiceState;
import edu.zieit.scheduler.schedule.course.CourseSchedule;
import edu.zieit.scheduler.util.ChatUtil;
import napi.configurate.yaml.lang.Language;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StateCourseList extends ChoiceState {

    private final Language lang;
    private final String prevPageText;
    private final String nextPageText;

    public StateCourseList(Language lang, State nextState) {
        super(nextState);
        this.lang = lang;
        prevPageText = lang.of("choice.prev");
        nextPageText = lang.of("choice.next");
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

            session.add("course", input.getUpdate()
                    .getCallbackQuery().getData());

            return InputResult.NEXT;
        }
        return InputResult.WRONG;
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

    private BotApiMethod buildListMessage(ChatSession session, int page) {
        ScheduleService service = session.getChatManager().getBot().getScheduleService();
        List<Pair<String, String>> courses = getCoursesList(service);
        return ChatUtil.editableMessage(session, buildKeyboard(page, courses),
                lang.of("cmd.course.list"));
    }

    private List<Pair<String, String>> getCoursesList(ScheduleService service) {
        Collection<Schedule> list = service.getCoursesSchedule();
        return list.stream()
                .map(schedule -> (CourseSchedule) schedule)
                .map(schedule -> Pair.of(schedule.getDisplayName(), schedule.getKey().toString()))
                .sorted(Comparator.comparing(Pair::key))
                .collect(Collectors.toList());
    }
}
