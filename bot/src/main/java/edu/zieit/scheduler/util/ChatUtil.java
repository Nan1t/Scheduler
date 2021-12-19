package edu.zieit.scheduler.util;

import edu.zieit.scheduler.bot.chat.ChatSession;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.io.InputStream;

public final class ChatUtil {

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
     * Build message that sends new or edit exists message with photo and caption
     * @param session Chat session
     * @param is Photo as stream
     * @param text Message text
     * @return Built method to send
     */
    public static PartialBotApiMethod[] editableMessage(ChatSession session, InputStream is, String filename, String text) {
        if (session.hasValidLastMsgId()) {
            return new PartialBotApiMethod[] {
                    DeleteMessage.builder()
                            .chatId(session.getChatId())
                            .messageId(session.getLastMsgId())
                            .build(),
                    /*SendPhoto.builder()
                            .chatId(session.getChatId())
                            .caption(text)
                            .photo(new InputFile(is, filename))
                            .build()*/
                    SendDocument.builder()
                            .chatId(session.getChatId())
                            .caption(text)
                            .document(new InputFile(is, filename))
                            .build()
            };
        } else {
            return new PartialBotApiMethod[] {
                    SendDocument.builder()
                            .chatId(session.getChatId())
                            .caption(text)
                            .document(new InputFile(is, filename))
                            .build()
            };
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
