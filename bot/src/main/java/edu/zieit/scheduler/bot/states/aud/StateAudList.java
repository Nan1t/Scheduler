package edu.zieit.scheduler.bot.states.aud;

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

public class StateAudList extends ChoiceState {

    public StateAudList(Language lang, State nextState) {
        super(lang, nextState);
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

            session.add("aud", input.getUpdate()
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
        List<Pair<String, String>> auds = getAudList(service);
        return ChatUtil.editableMessage(session, buildKeyboard(page, auds),
                lang.of("cmd.aud.list"));
    }

    private List<Pair<String, String>> getAudList(ScheduleService service) {
        return service.getClassrooms().stream()
                .map(aud -> Pair.of(aud, aud))
                .collect(Collectors.toList());
    }
}
