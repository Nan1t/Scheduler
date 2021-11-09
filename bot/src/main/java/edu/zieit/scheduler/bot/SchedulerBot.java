package edu.zieit.scheduler.bot;

import edu.zieit.scheduler.config.MainConfig;
import napi.configurate.yaml.lang.Language;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SchedulerBot extends TelegramLongPollingBot {

    private final String username;
    private final String token;
    private final Language lang;
    private final ChatManager chatManager;

    public SchedulerBot(MainConfig conf, Language lang) {
        this.username = conf.getTgBotName();
        this.token = conf.getTgToken();
        this.lang = lang;
        this.chatManager = new ChatManager(this, conf);
    }

    public Language getLang() {
        return lang;
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

    public void send(Object method) {
        if (method instanceof BotApiMethod apiMethod) {
            sendApiMethodAsync(apiMethod);
        }
    }

    public void sendMessage(String chatId, String msg) {
        send(SendMessage.builder()
                .chatId(chatId)
                .text(msg)
                .build());
    }

    public void shutdown() {
        chatManager.shutdownThreads();
        onClosing();
    }
}
