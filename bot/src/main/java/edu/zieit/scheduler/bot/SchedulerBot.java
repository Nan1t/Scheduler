package edu.zieit.scheduler.bot;

import edu.zieit.scheduler.api.schedule.ScheduleService;
import edu.zieit.scheduler.bot.chat.ChatManager;
import edu.zieit.scheduler.bot.chat.ChatSession;
import edu.zieit.scheduler.config.MainConfig;
import edu.zieit.scheduler.services.SubsService;
import napi.configurate.yaml.lang.Language;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SchedulerBot extends TelegramLongPollingBot {

    private final String username;
    private final String token;
    private final Language lang;
    private final ChatManager chatManager;
    private final ScheduleService scheduleService;
    private final SubsService subsService;

    public SchedulerBot(MainConfig conf, Language lang, ScheduleService scheduleService, SubsService subsService) {
        this.username = conf.getTgBotName();
        this.token = conf.getTgToken();
        this.lang = lang;
        this.chatManager = new ChatManager(this, conf);
        this.scheduleService = scheduleService;
        this.subsService = subsService;
    }

    public Language getLang() {
        return lang;
    }

    public ScheduleService getScheduleService() {
        return scheduleService;
    }

    public SubsService getSubsService() {
        return subsService;
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

    public void send(ChatSession session, Object[] methods) {
        for (Object method : methods) {
            send(session, method);
        }
    }

    public void send(ChatSession session, Object method) {
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
