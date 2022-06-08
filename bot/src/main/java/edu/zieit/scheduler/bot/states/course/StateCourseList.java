package edu.zieit.scheduler.bot.states.course;

import edu.zieit.scheduler.api.Pair;
import edu.zieit.scheduler.api.schedule.Schedule;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.state.State;
import edu.zieit.scheduler.bot.states.ListState;
import edu.zieit.scheduler.schedule.course.CourseSchedule;
import napi.configurate.yaml.lang.Language;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StateCourseList extends ListState {

    public StateCourseList(Language lang, State nextState) {
        super(lang, nextState);
    }

    @Override
    protected String caption() {
        return lang.of("cmd.course.list");
    }

    @Override
    protected void onSelected(ChatInput input, ChatSession session) {
        session.add("course", input.update()
                .getCallbackQuery().getData());
    }

    @Override
    protected List<Pair<String, String>> getData(ScheduleService service) {
        Collection<Schedule> list = service.getCoursesSchedule();
        return list.stream()
                .map(schedule -> (CourseSchedule) schedule)
                .map(schedule -> Pair.of(schedule.getDisplayName(), schedule.getKey().toString()))
                .sorted(Comparator.comparing(Pair::key))
                .collect(Collectors.toList());
    }
}
