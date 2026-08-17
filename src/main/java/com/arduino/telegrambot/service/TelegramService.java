package com.arduino.telegrambot.service;

import com.arduino.telegrambot.sender.BotSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.LinkPreviewOptions;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramService {

    @Autowired
    private BotSender botSender;

    public void sendMessage(
            Long chatId, String text, String parseMode) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setParseMode(parseMode);

        var linkPreviewOptions = new LinkPreviewOptions();
        linkPreviewOptions.setShowAboveText(false);
        linkPreviewOptions.setPreferLargeMedia(true);
        sendMessage.setLinkPreviewOptions(linkPreviewOptions);

        try {
            botSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(
                    "Failed to send message due to an error with the Telegram API.", e);
        }
    }

    public void sendMessageWithKeyboard(
            Long chatId,  InlineKeyboardMarkup keyboardMarkup, String text, String parseMode) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessage.setParseMode(parseMode);

        var linkPreviewOptions = new LinkPreviewOptions();
        linkPreviewOptions.setShowAboveText(false);
        linkPreviewOptions.setPreferLargeMedia(true);
        sendMessage.setLinkPreviewOptions(linkPreviewOptions);

        try {
            botSender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(
                    "Failed to send message due to an error with the Telegram API.", e);
        }
    }

    public void editMessage(
            Long chatId, int messageId, String text, InlineKeyboardMarkup keyboard, String parseMode) {

        var editMessage = new EditMessageText();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setText(text);
        editMessage.setParseMode(parseMode);
        editMessage.setReplyMarkup(keyboard);

        try {
            botSender.execute(editMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(
                    "Failed to send message due to an error with the Telegram API.", e);
        }
    }

    public void deleteMessage(
            Long chatId, int messageId) {

        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);

        try {
            botSender.execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(
                    "Failed to send message due to an error with the Telegram API.", e);
        }
    }
}
