package edu.zieit.scheduler.bot.chat;

import com.google.inject.Inject;
import com.google.inject.Provider;
import edu.zieit.scheduler.bot.Bot;
import edu.zieit.scheduler.bot.state.InputResult;
import edu.zieit.scheduler.bot.state.State;
import edu.zieit.scheduler.bot.state.StateRegistry;
import edu.zieit.scheduler.persistence.entity.BotUser;
import edu.zieit.scheduler.services.SubsService;
import edu.zieit.scheduler.api.config.Language;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.Map;

public class ChatManager {

    private static final Logger logger = LogManager.getLogger(ChatManager.class);

    private final Bot bot;
    private final Language lang;
    private final StateRegistry states;
    private final SubsService subsService;
    private final ChatSessionFactory sessionFactory;
    private final Map<String, ChatSession> sessions = new HashMap<>();

    @Inject
    public ChatManager(
            Bot bot,
            Language lang,
            StateRegistry states,
            SubsService subsService,
            Provider<ChatSessionFactory> sessionFactory
    ) {
        this.bot = bot;
        this.lang = lang;
        this.states = states;
        this.subsService = subsService;
        this.sessionFactory = sessionFactory.get();
    }

    public void handleUpdate(Update update) {
        String chatId = getChatId(update);

        if (chatId == null) return;

        ChatSession session = getOrCreateSession(update, chatId);
        ChatInput input = new ChatInput(chatId, session, update);

        if (update.hasMessage()) {
            session.resetLastMsgId();

            if (update.getMessage().isCommand()) {
                String cmdRaw = update.getMessage().getText();
                String[] arr = cmdRaw.split("@");

                if (arr.length > 1 && !arr[1].equalsIgnoreCase(bot.getUsername())) {
                    // Command not for this bot
                    endSession(chatId);
                    return;
                }

                String cmd = arr[0].substring(1);
                logger.info("Income command: '" + cmd + "' from " + getUsername(update));

                State state = states.getBaseState(cmd);

                if (state != null) {
                    session.updateState(state);

                    if (!state.hasNext())
                        endSession(chatId);
                } else {
                    bot.sendMessage(session, lang.of("cmd.unsupported"));
                }
                return;
            }
        }

        State state = session.getState();

        if (state != null) {
            InputResult result = state.input(input, session);

            if (result.equals(InputResult.STAY))
                return;

            if (result.equals(InputResult.NEXT)) {
                session.updateState(state.getNext());
                return;
            }

            if (!state.hasNext())
                endSession(chatId);
        }

        bot.sendMessage(session, lang.of("cmd.unsupported"));
    }

    private ChatSession getOrCreateSession(Update update, String chatId) {
        return sessions.computeIfAbsent(chatId, tgId -> {
            User user = getUser(update);

            if (user == null)
                throw new IllegalArgumentException("Bot user is null");

            BotUser botUser = subsService.getOrCreateUser(
                    tgId,
                    user.getUserName(),
                    user.getFirstName(),
                    user.getLastName()
            );
            return sessionFactory.createSession(tgId, botUser);
        });
    }

    private void endSession(String chatId) {
        sessions.remove(chatId);
    }

    private String getChatId(Update update) {
        if (update.hasMessage())
            return update.getMessage().getChatId().toString();
        else if (update.hasCallbackQuery())
            return update.getCallbackQuery().getMessage().getChatId().toString();
        return null;
    }

    private User getUser(Update update) {
        if (update.hasMessage())
            return update.getMessage().getFrom();
        else if (update.hasCallbackQuery())
            return update.getCallbackQuery().getFrom();
        return null;
    }

    private String getUsername(Update update) {
        String username = update.getMessage().getFrom().getUserName();
        String firstName = update.getMessage().getFrom().getFirstName();
        String lastName = update.getMessage().getFrom().getLastName();
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("[%s %s", firstName, lastName));

        if (username != null) {
            builder.append("(")
                    .append(username)
                    .append(")");
        }

        builder.append("]");

        return builder.toString();
    }
}
