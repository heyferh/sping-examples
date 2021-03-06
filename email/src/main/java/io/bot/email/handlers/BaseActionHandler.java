package io.bot.email.handlers;

import io.bot.email.model.Preferences;
import io.bot.email.model.SetupState;
import io.bot.email.model.Vendor;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static java.lang.StrictMath.toIntExact;

public class BaseActionHandler extends AbstractHandler {
    @Override
    boolean accept(Update update, Preferences preferences) {
        return (update.hasMessage()
                && update.getMessage().getText().equals("/actions")
                && preferences.getSetupState().equals(SetupState.SETUP_FINISHED))
                || (update.hasCallbackQuery()
                && (update.getCallbackQuery().getData().equals("backFromSettings")
                || update.getCallbackQuery().getData().equals("backInActions"))
                && preferences.getSetupState().equals(SetupState.SETUP_FINISHED));
    }

    @Override
    BotApiMethod handle(Update update, Preferences preferences) {
        Message message = update.hasMessage() ? update.getMessage() : update.getCallbackQuery().getMessage();
        if (update.hasMessage())
            return new SendMessage()
                    .setReplyMarkup(getInlineKeyboard())
                    .setChatId(message.getChatId())
                    .setText("Select prefered action :e-mail:");
        else
        return new EditMessageText()
                .setReplyMarkup(getInlineKeyboard())
                .setChatId(message.getChatId())
                .setMessageId(toIntExact(message.getMessageId()))
                .setText("Select prefered action :e-mail:");
    }

    private InlineKeyboardMarkup getInlineKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Inbox").setCallbackData("inbox"));
        rowInline.add(new InlineKeyboardButton().setText("Outbox").setCallbackData("outbox"));
        rowsInline.add(rowInline);
        rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("Unread").setCallbackData("unread"));
        rowInline.add(new InlineKeyboardButton().setText("Settings").setCallbackData("settings"));
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
