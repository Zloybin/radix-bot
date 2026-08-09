package com.arduino.telegrambot;

import com.arduino.telegrambot.dispatcher.Dispatcher;
import com.arduino.telegrambot.enummeration.AnswerType;
import com.arduino.telegrambot.enummeration.TaskLevel;
import com.arduino.telegrambot.model.Task;
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

//    @Autowired
//    private ArduinoSerialService arduinoService;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private UserService userService;

    @Autowired
    private Dispatcher dispatcher;

    @Autowired
    private TaskRepository taskRepository;

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


        var list = taskRepository.findAll();
        for (Task task : list) {
            System.out.println(task);
        }

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

        if (!userService.isUserExist(userRequest.getChatId())) {
            var chatId = userRequest.getChatId();
            userService.buildAndPutDefaultUser(chatId);
        }

        dispatcher.dispatch(userRequest);


    }

//    private void handleMainMenu(long chatId, String text) {
//        switch (text) {
//            case "/start" -> {
//                userStateService.setUserState(chatId, MenuType.MAIN);
//                sendMessageWithKeyboard(chatId, "Привет! Выберите действие:", KeyboardBuilder.buildMainMenu());
//            }
//            case "🔧 Управление двигателем" -> {
//                userStateService.setUserState(chatId, MenuType.ENGINE_CONTROL);
//                sendMessageWithKeyboard(chatId, "Управление двигателем:", KeyboardBuilder.buildEngineMenu());
//            }
//            default -> sendMessageWithKeyboard(chatId, "Неизвестная команда. Используйте /start", KeyboardBuilder.buildMainMenu());
//        }
//    }

//    private void handleEngineMenu(long chatId, String text) {
//        switch (text) {
//            case "⬆️ Вперед" -> {
//                System.out.println("🚗 Двигатель едет вперед");
//                arduinoService.sendCommand("start");
//                sendMessage(chatId, "✅ Двигатель едет вперед");
//            }
//            case "⬇️ Назад" -> {
//                System.out.println("🔙 Двигатель едет назад");
//                arduinoService.sendCommand("stop");
//                sendMessage(chatId, "✅ Двигатель едет назад");
//            }
//            case "🔙 Назад в меню" -> {
//                userStateService.setUserState(chatId, MenuType.MAIN);
//                sendMessageWithKeyboard(chatId, "Главное меню:", KeyboardBuilder.buildMainMenu());
//            }
//            default -> sendMessageWithKeyboard(chatId, "Неизвестная команда:", KeyboardBuilder.buildEngineMenu());
//        }
//    }
//
//    private void handleArduinoMessage(String message) {
//        if (adminChatId != null && message.startsWith("STATUS:")) {
//            String readableStatus = message.replace("STATUS:", "🤖 ");
//            sendMessage(adminChatId, readableStatus);
//        }
//    }


    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setParseMode(ParseMode.MARKDOWN);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения", e);
        }
    }

    private void sendMessageWithKeyboard(long chatId, String text, org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(keyboard);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения с клавиатурой", e);
        }
    }
}