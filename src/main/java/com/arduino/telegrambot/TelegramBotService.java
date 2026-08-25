package com.arduino.telegrambot;

import com.arduino.telegrambot.dispatcher.Dispatcher;
import com.arduino.telegrambot.entity.Result;
import com.arduino.telegrambot.entity.Task;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.properties.AppProperties;
import com.arduino.telegrambot.repository.ResultRepository;
import com.arduino.telegrambot.repository.UserRepository;
import com.arduino.telegrambot.service.TaskService;
import com.arduino.telegrambot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@Component
@Slf4j
public class TelegramBotService extends TelegramLongPollingBot {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private UserService userService;

    @Autowired
    private Dispatcher dispatcher;

    @Autowired
    private UserRepository userRepository;

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

        userRepository.deleteFromUser();

        var userRequestBuilder = UserRequest.builder();

        String name;

        if (update.hasMessage() && update.getMessage().hasText()) {
            name = update.getMessage().getFrom().getUserName();

            userRequestBuilder
                    .chatId(update.getMessage().getChatId())
                    .messageId(update.getMessage().getMessageId())
                    .request(update.getMessage().getText());

        } else if (update.hasCallbackQuery()) {
            name = update.getCallbackQuery().getFrom().getUserName();

            userRequestBuilder
                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .request(update.getCallbackQuery().getData());
        } else {
            throw new RuntimeException("Неисправное состояние объекта класса update.");
        }

        var userRequest = userRequestBuilder.build();
        System.out.println(userRequest.getChatId());

        Long chatId = userRequest.getChatId();
        if (!userService.existById(chatId)){
            var user = userService.buildDefaultUser(chatId, name);
            User save = userService.save(user);
            System.out.println(String.format("User с id:%d был зарегестрирован и добавлен в БД.", save.getId()));
        }

        dispatcher.dispatch(userRequest);

    }
}