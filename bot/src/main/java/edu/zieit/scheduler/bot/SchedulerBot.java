package edu.zieit.scheduler.bot;

import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatManager;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.config.MainConfig;
import napi.configurate.yaml.lang.Language;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

import java.io.Serializable;

public class SchedulerBot extends TelegramLongPollingBot {

    private final String username;
    private final String token;
    private final Language lang;
    private final ChatManager chatManager;
    private final ScheduleService scheduleService;

    public SchedulerBot(MainConfig conf, Language lang, ScheduleService scheduleService) {
        this.username = conf.getTgBotName();
        this.token = conf.getTgToken();
        this.lang = lang;
        this.chatManager = new ChatManager(this, conf);
        this.scheduleService = scheduleService;
    }

    public Language getLang() {
        return lang;
    }

    public ScheduleService getScheduleService() {
        return scheduleService;
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
    public void onUpdateReceived(Update update) {
        chatManager.handleUpdate(update);
    }

    public void send(ChatSession session, Object method) {
        if (method instanceof BotApiMethod apiMethod) {
            sendApiMethodAsync(apiMethod, new LastMessageCallback(session));
        }
    }

    public void sendMessage(ChatSession session, String msg) {
        send(session, SendMessage.builder()
                .chatId(session.getChatId())
                .text(msg)
                .build());
    }

    public void shutdown() {
        chatManager.shutdownThreads();
        onClosing();
    }
}
