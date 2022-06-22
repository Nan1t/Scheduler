package edu.zieit.scheduler.bot;

import com.google.inject.Inject;
import edu.zieit.scheduler.bot.chat.ChatManager;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.config.MainConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

public class SchedulerBot extends TelegramLongPollingBot implements Bot {

    private static final Logger logger = LogManager.getLogger(SchedulerBot.class);

    private static final int MAX_PER_SECOND = 25;

    private final String username;
    private final String token;

    private final ExecutorService threadPool;
    private final ChatManager chatManager;
    private final Queue<SendMethod> sendQueue = new LinkedList<>();

    private final ScheduledExecutorService timer;
    private final ScheduledFuture<?> sendTask;

    @Inject
    public SchedulerBot(MainConfig conf, ChatManager chatManager) {
        this.username = conf.getTgBotName();
        this.token = conf.getTgToken();
        this.chatManager = chatManager;
        this.threadPool = Executors.newFixedThreadPool(conf.getThreadPoolSize());
        this.timer = Executors.newScheduledThreadPool(1);
        this.sendTask = timer.scheduleAtFixedRate(this::sendFromQueue,
                0L, 1L, TimeUnit.SECONDS);
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        CompletableFuture.runAsync(() -> {
            try {
                chatManager.handleUpdate(update);
            } catch (Throwable t) {
                logger.error("Update handling error:", t);
            }
        }, threadPool);
    }

    @Override
    public void send(ChatSession session, Object... methods) {
        for (Object method : methods) {
            if (method != null)
                sendQueue.offer(new SendMethod(session, method));
        }
    }

    @Override
    public void send(Object method) {
        send(null, method);
    }

    @Override
    public void sendMessage(ChatSession session, String msg) {
        send(session, SendMessage.builder()
                .chatId(session.getChatId())
                .text(msg)
                .build());
    }

    private void sendNow(ChatSession session, Object method) {
        if (method instanceof BotApiMethod apiMethod) {
            sendApiMethodAsync(apiMethod, new LastMessageCallback(session));
            return;
        }
        if (method instanceof SendPhoto sendPhoto) {
            executeAsync(sendPhoto);
            return;
        }
        if (method instanceof SendDocument sendDoc) {
            executeAsync(sendDoc);
            return;
        }
        if (method instanceof EditMessageMedia editMedia) {
            executeAsync(editMedia);
            return;
        }
        logger.warn("Attempt to send undefined method to Telegram user: " + method.getClass());
    }

    private void sendFromQueue() {
        if (!exe.isShutdown()) {
            for (int i = 0; i < MAX_PER_SECOND; i++) {
                SendMethod method = sendQueue.poll();

                if (method != null)
                    sendNow(method.session(), method.method());
            }
        }
    }

    public void shutdown() {
        sendTask.cancel(false);
        timer.shutdown();
        if (threadPool != null)
            threadPool.shutdown();
        onClosing();
    }
}
