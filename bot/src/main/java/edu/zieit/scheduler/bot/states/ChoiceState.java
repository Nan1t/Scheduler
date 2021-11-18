package edu.zieit.scheduler.bot.states;

import edu.zieit.scheduler.api.Pair;
import edu.zieit.scheduler.bot.chat.State;
import napi.configurate.yaml.lang.Language;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

public abstract class ChoiceState extends State {

    protected final Language lang;
    private final String prevPageText;
    private final String nextPageText;
    private final String firstPageText;
    private final String lastPageText;

    public ChoiceState(Language lang, State next) {
        super(next);
        this.lang = lang;
        prevPageText = lang.of("choice.prev");
        nextPageText = lang.of("choice.next");
        firstPageText = lang.of("choice.first");
        lastPageText = lang.of("choice.last");
    }

    private static final String SEPARATOR = "\00";

    protected abstract int elemOnPage();

    protected InlineKeyboardMarkup buildKeyboard(int page, List<Pair<String, String>> data) {
        var builder = InlineKeyboardMarkup.builder();
        int pages = (int) Math.ceil((float) data.size() / elemOnPage());
        int from = Math.max(0, elemOnPage() * page);
        int to = Math.min(from + elemOnPage(), data.size());
        List<Pair<String, String>> pageData = data.subList(from, to);
        List<InlineKeyboardButton> row = new LinkedList<>();

        int i = 0;
        for (Pair<String, String> pair : pageData) {
            if (i % 2 == 0) {
                builder.keyboardRow(row);
                row = new LinkedList<>();
            }

            row.add(InlineKeyboardButton.builder()
                    .text(pair.key())
                    .callbackData(pair.value())
                    .build());
            i++;
        }

        if (!row.isEmpty())
            builder.keyboardRow(row);

        if (page >= pages - 1) {
            // Last page
            builder.keyboardRow(Arrays.asList(
                    getPageBtn(firstPageText, 0),
                    getPageBtn(prevPageText, page - 1)
            ));
        } else if (page == 0) {
            // First page
            builder.keyboardRow(Arrays.asList(
                    getPageBtn(nextPageText, page + 1),
                    getPageBtn(lastPageText, pages - 1)
            ));
        } else {
            builder.keyboardRow(Arrays.asList(
                    getPageBtn(firstPageText, 0),
                    getPageBtn(prevPageText, page - 1),
                    getPageBtn(nextPageText, page + 1),
                    getPageBtn(lastPageText, pages - 1)
            ));
        }

        return builder.build();
    }

    protected int getPageCmd(Update update) {
        if (update.hasCallbackQuery()) {
            String[] cmd = update.getCallbackQuery().getData().split(SEPARATOR);
            if (cmd[0].equals("page")) {
                return Integer.parseInt(cmd[1]);
            }
        }
        return -1;
    }

    private InlineKeyboardButton getPageBtn(String text, int page) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData("page" + SEPARATOR + page)
                .build();
    }

}
