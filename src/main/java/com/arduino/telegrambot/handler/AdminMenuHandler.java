package com.arduino.telegrambot.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class AdminMenuHandler implements UpdateHandler{

    @Autowired
    private UserService userService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "adminMenu".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var keyboard = keyboardBuilder.buildAdminMenuKeyboard();
        var text = templateProcessor.processAdminMenuTemplate();
        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);

    }
}
