package edu.zieit.scheduler.bot;

import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.bot.chat.InputContext;
import edu.zieit.scheduler.bot.chat.State;
import edu.zieit.scheduler.bot.states.teachers.StateTeacherSelect;
import edu.zieit.scheduler.config.MainConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatManager {

    private static final Logger logger = LogManager.getLogger(ChatManager.class);
    private static final Map<String, State> STATE_REGISTRY = new HashMap<>();

    private final SchedulerBot bot;
    private final ExecutorService threadPool;
    private final Map<String, ChatSession> sessions;

    public ChatManager(SchedulerBot bot, MainConfig conf) {
        this.bot = bot;
        threadPool = Executors.newFixedThreadPool(conf.getThreadPoolSize());
        sessions = new HashMap<>();
    }

    public void handleUpdate(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        ChatSession session = getOrCreateSession(chatId);
        InputContext ctx = new InputContext(session, this, update);

        if (update.hasMessage() && update.getMessage().isCommand()) {
            String cmdRaw = update.getMessage().getText();
            String[] arr = cmdRaw.split("@");

            if (arr.length > 1 && !arr[1].equalsIgnoreCase(bot.getBotUsername())) {
                // Command not for this bot
                endSession(chatId);
                return;
            }

            String cmd = arr[0].substring(1);
            logger.info("Income command: '" + cmd + "'");
            State state = getStaticState(cmd);

            if (state != null) {
                bot.send(session.updateState(ctx, state));
                return;
            }
        }

        Object msg = session.onInput(ctx);

        if (msg != null) {
            bot.send(session.onInput(ctx));
        } else {
            bot.sendMessage(chatId, "Undefined command or argument. Check help with /help");
        }
    }

    public void shutdownThreads() {
        if (threadPool != null)
            threadPool.shutdown();
    }

    private ChatSession getOrCreateSession(String chatId) {
        return sessions.computeIfAbsent(chatId, ChatSession::new);
    }

    private void endSession(String chatId) {
        sessions.remove(chatId);
    }

    public static State getStaticState(String cmd) {
        return STATE_REGISTRY.get(cmd.toLowerCase());
    }

    public static void regStaticState(String cmd, State state) {
        STATE_REGISTRY.put(cmd.toLowerCase(), state);
    }

    static {
        regStaticState("tsub", new StateTeacherSelect());
    }
}
