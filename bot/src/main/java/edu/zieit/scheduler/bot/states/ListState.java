package edu.zieit.scheduler.bot.states;

import edu.zieit.scheduler.api.Pair;
import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.state.InputResult;
import edu.zieit.scheduler.bot.state.State;
import edu.zieit.scheduler.util.ChatUtil;
import napi.configurate.yaml.lang.Language;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

public abstract class ListState extends ChoiceState {

    public ListState(Language lang, State next) {
        super(lang, next);
    }

    @Override
    public void activate(ChatSession session) {
        session.reply(buildListMessage(session, 0, true));
    }

    @Override
    public InputResult input(ChatInput input, ChatSession session) {
        if (input.getUpdate().hasCallbackQuery()) {
            int page = getPageCmd(input.getUpdate());

            if (page != -1) {
                session.reply(buildListMessage(session, page, false));
                return InputResult.STAY;
            }

            onSelected(input, session);

            return InputResult.NEXT;
        }

        return InputResult.WRONG;
    }

    @Override
    protected int elemOnPage() {
        return 16;
    }

    private BotApiMethod buildListMessage(ChatSession session, int page, boolean first) {
        ScheduleService service = session.getScheduleService();
        List<Pair<String, String>> courses = getData(service);
        InlineKeyboardMarkup keyboard = buildKeyboard(page, courses);
        return ChatUtil.editableMessage(session, keyboard, caption());
    }

    protected abstract String caption();

    protected abstract void onSelected(ChatInput input, ChatSession session);

    protected abstract List<Pair<String, String>> getData(ScheduleService service);

}
