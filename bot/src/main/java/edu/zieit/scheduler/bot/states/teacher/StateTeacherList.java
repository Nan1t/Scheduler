package edu.zieit.scheduler.bot.states.teacher;

import edu.zieit.scheduler.api.Pair;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.state.State;
import edu.zieit.scheduler.bot.states.ListState;
import edu.zieit.scheduler.schedule.teacher.TeacherSchedule;
import napi.configurate.yaml.lang.Language;

import java.util.List;
import java.util.stream.Collectors;

public class StateTeacherList extends ListState {

    public StateTeacherList(Language lang, State nextState) {
        super(lang, nextState);
    }

    @Override
    protected String caption() {
        return lang.of("cmd.teacher.list");
    }

    @Override
    protected void onSelected(ChatInput input, ChatSession session) {
        session.add("teacher", input.update()
                .getCallbackQuery().getData());
    }

    @Override
    protected List<Pair<String, String>> getData(ScheduleService service) {
        var schedule = (TeacherSchedule) service.getTeacherSchedule();
        return schedule.getTeachers().stream()
                .map(str -> Pair.of(str, str))
                .collect(Collectors.toList());
    }
}
