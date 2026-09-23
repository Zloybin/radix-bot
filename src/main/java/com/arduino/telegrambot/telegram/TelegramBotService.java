package com.arduino.telegrambot.telegram;

import com.arduino.telegrambot.dispatcher.Dispatcher;
import com.arduino.telegrambot.entity.DeckProgress;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.enummeration.DeckStatus;
import com.arduino.telegrambot.feature.anki.service.AnkiService;
import com.arduino.telegrambot.feature.anki.util.AnkiUtility;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.telegram.properties.BotProperties;
import com.arduino.telegrambot.service.CallbackService;
import com.arduino.telegrambot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TelegramBotService extends TelegramLongPollingBot {

    @Autowired
    private BotProperties botProperties;

    @Autowired
    private UserService userService;

    @Autowired
    private Dispatcher dispatcher;

    @Autowired
    private CallbackService callbackService;

    @Autowired
    private AnkiService ankiService;



    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {


        var userRequestBuilder = UserRequest.builder();

        String name;

        if (update.hasMessage() && update.getMessage().hasText()) {
            name = update.getMessage().getFrom().getUserName();

            userRequestBuilder
                    .chatId(update.getMessage().getChatId())
                    .messageId(update.getMessage().getMessageId())
                    .handler(update.getMessage().getText());

        } else if (update.hasCallbackQuery()) {
            name = update.getCallbackQuery().getFrom().getUserName();
            var hash = update.getCallbackQuery().getData().toString();

            var callback = callbackService.findById(Long.parseLong(hash));

            userRequestBuilder
                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .handler(callback.getHandler())
                    .request(callback.getRequest());
        } else {
            throw new RuntimeException("Неисправное состояние объекта класса update.");
        }


        var userRequest = userRequestBuilder.build();
        Long chatId = userRequest.getChatId();

        var decks = ankiService.getDecks().block();
        var filteredDeckList = decks.stream().filter(deck -> !AnkiUtility.EXCLUDED_DECKS.contains(deck)).toList();

        User userz = userService.findById(userRequest.getChatId());
        List<DeckProgress> deckProgressList = new ArrayList<>();
        for (String deck : filteredDeckList) {
            var deckProgress = DeckProgress.builder()
                    .deckStatus(DeckStatus.NOT_STARTED)
                    .localDate(0L)
                    .deckName(deck)
                    .user(userz)
                    .build();

            deckProgressList.add(deckProgress);
        }

        userz.setDeckProgress(deckProgressList);


//        User userT = userService.findById(userRequest.getChatId());
//        userT.setState(UserState.FREE);
//        userService.save(userT);


        if (!userService.existById(chatId)){
            var user = userService.buildDefaultUser(chatId, name);
            User save = userService.save(user);
            System.out.println(String.format("User с id:%d был зарегестрирован и добавлен в БД.", save.getId()));
        }

        dispatcher.dispatch(userRequest);

    }
}