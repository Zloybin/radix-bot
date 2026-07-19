package com.arduino.telegrambot.handle;

import com.arduino.telegrambot.service.TelegramBotService;
import com.arduino.telegrambot.service.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;

@Component
public class StartCommandHandler implements UpdateHandler {
    private final String handlerCallback = "/start";

    @Autowired
    private TemplateEngine engine;

    @Autowired
    private TelegramService telegramService;



    @Override
    public boolean isApplicable(String callback) {
        return handlerCallback.equals(callback);
    }

    @Override
    public void handle(Update update) {
        Context context = new Context();

        context.setVariable("userName", "Иван");
        context.setVariable("balance", 1500);
        String test = engine.process("test", context);


        telegramService.sendMessage(update.getMessage().getChatId(),test, ParseMode.MARKDOWN);
    }
}
