package edu.zieit.scheduler.bot.states.group;

import edu.zieit.scheduler.api.Pair;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.bot.states.ListState;
import napi.configurate.yaml.lang.Language;

import java.util.List;
import java.util.stream.Collectors;

public class StateGroupList extends ListState {

    public StateGroupList(Language lang, State next) {
        super(lang, next);
    }

    @Override
    protected String caption() {
        return lang.of("cmd.group.list");
    }

    @Override
    protected void onSelected(ChatInput input, ChatSession session) {
        session.add("group", input.getUpdate()
                .getCallbackQuery().getData());
    }

    @Override
    protected List<Pair<String, String>> getData(ScheduleService service) {
        return service.getGroups().stream()
                .map(str -> Pair.of(str.toUpperCase(), str))
                .collect(Collectors.toList());
    }
}
