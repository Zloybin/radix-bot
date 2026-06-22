package com.arduino.telegrambot.config;

import com.arduino.telegrambot.service.TelegramBotService;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import jakarta.annotation.PostConstruct;

@Configuration
public class TelegramBotConfig {

    private final TelegramBotService telegramBotService;

    // Конструктор инжектит существующий бин, а не создаёт новый
    public TelegramBotConfig(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBotService);
            System.out.println("✅ Telegram бот зарегистрирован успешно");
        } catch (TelegramApiException e) {
            throw new RuntimeException("Ошибка регистрации бота", e);
        }
    }
}