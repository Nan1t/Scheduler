package edu.zieit.scheduler.util;

import edu.zieit.scheduler.bot.chat.ChatInput;
import edu.zieit.scheduler.bot.chat.ChatSession;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class ChatUtil {

    private ChatUtil() {}

    /**
     * Build message that sends new or edit exists message with keyboard and text
     * @param session Chat session
     * @param kb Keyboard instance
     * @param text Message text
     * @return Built method to send
     */
    public static BotApiMethod editableMessage(ChatSession session, InlineKeyboardMarkup kb, String text) {
        if (session.hasValidLastMsgId()) {
            return EditMessageText.builder()
                    .chatId(session.getChatId())
                    .messageId(session.getLastMsgId())
                    .replyMarkup(kb)
                    .text(text)
                    .build();
        } else {
            return SendMessage.builder()
                    .chatId(session.getChatId())
                    .replyMarkup(kb)
                    .text(text)
                    .build();
        }
    }

    /**
     * Return keyboard markup that clears keyboard buttons
     * @return Keyboard markup
     */
    public static InlineKeyboardMarkup clearKeyboard() {
        return InlineKeyboardMarkup.builder()
                .clearKeyboard()
                .build();
    }

}
