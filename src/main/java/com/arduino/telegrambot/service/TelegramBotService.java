package com.arduino.telegrambot.service;

import com.arduino.telegrambot.enummeration.UserState;
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
import org.thymeleaf.context.Context;;

@Component
@Slf4j
public class TelegramBotService extends TelegramLongPollingBot {

//    @Autowired
//    private ArduinoSerialService arduinoService;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private UserDBService userDBService;

    @Autowired
    private TemplateEngine templateEngine;

//    @PostConstruct
//    public void init() {
//        arduinoService.setMessageListener(this::handleArduinoMessage);
//        log.info("🤖 Бот {} инициализирован", appProperties.getName());
//    }

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
        if (update.hasMessage() && update.getMessage().hasText()) {

            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

//            var user = userDBService.getOrDefault(chatId);

            if ("/start".equals(text)){

                Context context = new Context();

                context.setVariable("userName", "Иван");
                context.setVariable("balance", 1500);
                String test = templateEngine.process("test", context);
                sendMessage(chatId, test);
/*
                UserState.FREE.name().equals(user.getState()) ?
                //  отправляем стартовое меню :
                //  сообщение "У вас осталось невыполненное задание". Опции: перейти к заданию, сбросить задание;

            } else {

                var state = user.getState();
                if (UserState.FREE.equals(state)) {
                    //  Неверный ввод. У вас нет задания. Для того чтобы начать задание нажмите кнопку
                }else {
                    // обработка ответа -> отправка результата
                }*/
            }




//            log.info("📨 Сообщение от {}: {} (Текущее меню: {})", chatId, text, currentState);

//            if (currentState == MenuType.MAIN) {
//                handleMainMenu(chatId, text);
//            } else if (currentState == MenuType.ENGINE_CONTROL) {
//                handleEngineMenu(chatId, text);
//            }
        }
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