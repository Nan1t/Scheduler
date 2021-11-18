package edu.zieit.scheduler.bot.states.group;

import edu.zieit.scheduler.api.Pair;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputResult;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.bot.states.ChoiceState;
import edu.zieit.scheduler.util.ChatUtil;
import napi.configurate.yaml.lang.Language;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import java.util.List;
import java.util.stream.Collectors;

public class StateGroupList extends ChoiceState {

    public StateGroupList(Language lang, State next) {
        super(lang, next);
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

            session.add("group", input.getUpdate()
                    .getCallbackQuery().getData());

            return InputResult.NEXT;
        }
        return InputResult.WRONG;
    }

    @Override
    protected int elemOnPage() {
        return 16;
    }

    private BotApiMethod buildListMessage(ChatSession session, int page) {
        ScheduleService service = session.getChatManager().getBot().getScheduleService();
        List<Pair<String, String>> groups = getGroupsList(service);
        return ChatUtil.editableMessage(session, buildKeyboard(page, groups),
                "Groups list");
    }

    private List<Pair<String, String>> getGroupsList(ScheduleService service) {
        return service.getGroups().stream()
                .map(str -> Pair.of(str.toUpperCase(), str))
                .collect(Collectors.toList());
    }
}
