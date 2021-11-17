package edu.zieit.scheduler.bot.chat;

import edu.zieit.scheduler.bot.SchedulerBot;
import edu.zieit.scheduler.bot.states.students.StateStudentList;
import edu.zieit.scheduler.bot.states.students.StateStudentShow;
import edu.zieit.scheduler.bot.states.teacher.*;
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

    private final SchedulerBot bot;
    private final ExecutorService threadPool;
    private final Map<String, ChatSession> sessions;
    private final Map<String, State> baseStates = new HashMap<>();

    public ChatManager(SchedulerBot bot, MainConfig conf) {
        this.bot = bot;
        threadPool = Executors.newFixedThreadPool(conf.getThreadPoolSize());
        sessions = new HashMap<>();
        registerStates();
    }

    public SchedulerBot getBot() {
        return bot;
    }

    public State getBaseState(String cmd) {
        return baseStates.get(cmd.toLowerCase());
    }

    public void registerState(State state, String... aliases) {
        for (String alias : aliases) {
            baseStates.put(alias.toLowerCase(), state);
        }
    }

    public void handleUpdate(Update update) {
        String chatId = getChatId(update);

        if (chatId == null) return;

        ChatSession session = getOrCreateSession(chatId);
        ChatInput input = new ChatInput(chatId, session, this, update);

        if (update.hasMessage()) {
            session.resetLastMsgId();

            if (update.getMessage().isCommand()) {
                String cmdRaw = update.getMessage().getText();
                String[] arr = cmdRaw.split("@");

                if (arr.length > 1 && !arr[1].equalsIgnoreCase(bot.getBotUsername())) {
                    // Command not for this bot
                    endSession(chatId);
                    return;
                }

                String cmd = arr[0].substring(1);
                logger.info("Income command: '" + cmd + "'");

                State state = getBaseState(cmd);

                if (state != null) {
                    session.updateState(state);
                    if (!state.hasNext())
                        endSession(chatId);
                } else {
                    bot.sendMessage(session, bot.getLang().of("cmd.unsupported"));
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
        }

        bot.sendMessage(session, "Invalid request");
    }

    public void shutdownThreads() {
        if (threadPool != null)
            threadPool.shutdown();
    }

    private ChatSession getOrCreateSession(String chatId) {
        return sessions.computeIfAbsent(chatId, id -> new ChatSession(this, id));
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

    private void registerStates() {
        registerState(new StateTeacherList(bot.getLang(), new StateTeacherSubs()), "teachersubscribe", "tsub");
        registerState(new StateTeacherList(bot.getLang(), new StateTeacherShow()), "tshow");
        registerState(new StateTeacher(), "teacher");
        registerState(new StateTeacherDeny(), "tdeny");
        registerState(new StateToggleNotices(), "notices");

        registerState(new StateStudentList(bot.getLang(), new StateStudentShow()), "stshow");
    }
}
