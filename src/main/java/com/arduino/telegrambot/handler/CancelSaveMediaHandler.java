package com.arduino.telegrambot.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class CancelSaveMediaHandler implements UpdateHandler{

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
        return "cancelSaveMedia".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        user.setState(UserState.FREE);
        userService.save(user);

        var keyboard = keyboardBuilder.buildAdminMenuKeyboard();
        var text = templateProcessor.processAdminMenuTemplate();

        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);

    }
}
