package com.arduino.telegrambot.telegram;

import com.arduino.telegrambot.dispatcher.Dispatcher;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.anki.service.AnkiServiceImpl;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.telegram.properties.BotProperties;
import com.arduino.telegrambot.service.CallbackService;
import com.arduino.telegrambot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Comparator;
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
    private AnkiServiceImpl ankiService;



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

        String firstname;
        String lastname;
        String username;

        if (update.hasMessage()) {
            Message message = update.getMessage();
            firstname = message.getFrom().getFirstName();
            lastname = message.getFrom().getLastName();
            username = message.getFrom().getUserName();
            if (update.getMessage().hasPhoto()) {

                var caption = message.getCaption();

                List<PhotoSize> photos = message.getPhoto();
                PhotoSize largest = photos.stream()
                        .max(Comparator.comparing(p -> p.getFileSize() != null ? p.getFileSize() : 0))
                        .orElseThrow();

                String fileId = largest.getFileId();

                userRequestBuilder
                        .chatId(update.getMessage().getChatId())
                        .messageId(update.getMessage().getMessageId())
                        .handler(caption)
                        .request(fileId);

            } else if (update.getMessage().hasText()) {
                userRequestBuilder
                        .chatId(message.getChatId())
                        .messageId(message.getMessageId())
                        .handler(message.getText());

            } else {
                throw new RuntimeException("Message не содержит ни текста ни изображения.");
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();

            firstname = callbackQuery.getFrom().getFirstName();
            lastname = callbackQuery.getFrom().getLastName();
            username = callbackQuery.getFrom().getUserName();
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

        if (!userService.existById(chatId)) {

            var user = userService.buildDefaultUser(chatId, firstname, lastname, username);
            User save = userService.save(user);
            System.out.println(String.format("User с id:%d был зарегестрирован и добавлен в БД.", save.getId()));
        }

        dispatcher.dispatch(userRequest);

    }


    public String uploadPhoto(File file, Long chatId) throws TelegramApiException {
        SendPhoto request = SendPhoto.builder()
                .chatId(String.valueOf(chatId))
                .photo(new InputFile(file))
                .build();

        Message sent = execute(request);

        // Telegram возвращает несколько размеров одной картинки, последний — самый большой
        List<PhotoSize> sizes = sent.getPhoto();
        String fileId = sizes.get(sizes.size() - 1).getFileId();

        System.out.println("file_id: " + fileId);
        return fileId;
    }

    private File getFileFromResource(String link) {
        var url = getClass().getClassLoader().getResource(link);
        if (url == null) {
            try {
                throw new FileNotFoundException(String.format("Ресурс %s не найден", link));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            return new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private String saveImageAndGetFileId(String link, long chatId) {
        var file = getFileFromResource(link);
        try {
            return uploadPhoto(file, chatId);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}