package com.arduino.telegrambot;

import com.arduino.telegrambot.dispatcher.Dispatcher;
import com.arduino.telegrambot.enummeration.AnswerType;
import com.arduino.telegrambot.enummeration.TaskLevel;
import com.arduino.telegrambot.model.Task;
import com.arduino.telegrambot.model.User;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.properties.AppProperties;
import com.arduino.telegrambot.repository.TaskRepository;
import com.arduino.telegrambot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TelegramBotService extends TelegramLongPollingBot {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private UserService userService;

    @Autowired
    private Dispatcher dispatcher;

    @Override
    public String getBotUsername() {
        return appProperties.getName();
    }

    @Override
    public String getBotToken() {
        return appProperties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        var userRequestBuilder = UserRequest.builder();

        if (update.hasMessage() && update.getMessage().hasText()) {
            userRequestBuilder.chatId(update.getMessage().getChatId())
                    .request(update.getMessage().getText());
        } else if (update.hasCallbackQuery()) {
            userRequestBuilder.chatId(update.getCallbackQuery().getMessage().getChatId())
                    .request(update.getCallbackQuery().getData());
        } else {
            throw new RuntimeException("Неисправное состояние объекта класса update.");
        }

        var userRequest = userRequestBuilder.build();

        Long chatId = userRequest.getChatId();
        if (!userService.existById(chatId)){
            var user = userService.buildDefaultUser(chatId);
            User save = userService.save(user);
            System.out.println();
        }

        System.out.println(userService.existById(chatId));


        dispatcher.dispatch(userRequest);


    }
}