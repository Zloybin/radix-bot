package com.arduino.telegrambot.service;

import com.arduino.telegrambot.sender.BotSender;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.LinkPreviewOptions;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.handle.Dispatcher;
import com.arduino.telegrambot.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.keyboard.MenuType;
import com.arduino.telegrambot.model.User;
import com.arduino.telegrambot.properties.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class TelegramService {

    @Autowired
    private BotSender botSender;

    public void sendMessage(
            Long chatId, /*String caption, InlineKeyboardMarkup keyboardMarkup,*/ String text, String parseMode) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
//        sendMessage.setReplyMarkup(keyboardMarkup);
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
}
