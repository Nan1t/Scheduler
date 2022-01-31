package edu.zieit.scheduler.bot.states.aud;

import edu.zieit.scheduler.api.Pair;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.bot.states.ListState;
import napi.configurate.yaml.lang.Language;

import java.util.List;
import java.util.stream.Collectors;

public class StateAudList extends ListState {

    public StateAudList(Language lang, State nextState) {
        super(lang, nextState);
    }

    @Override
    protected String caption() {
        return lang.of("cmd.aud.list");
    }

    @Override
    protected void onSelected(ChatInput input, ChatSession session) {
        session.add("aud", input.getUpdate()
                .getCallbackQuery().getData());
    }

    @Override
    protected List<Pair<String, String>> getData(ScheduleService service) {
        return service.getClassrooms().stream()
                .map(aud -> Pair.of(aud, aud))
                .collect(Collectors.toList());
    }
}
