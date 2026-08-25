package com.arduino.telegrambot.handle.physik;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class ExcludeCompletedTaskHandler implements UpdateHandler {

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private UserService userService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "excludeCompletedTask".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());

        boolean excluded = user.isExcluded();
        boolean newValue = !excluded;
        user.setExcluded(newValue);
        userService.save(user);

        var keyboard = keyboardBuilder.buildSettingMenu(newValue);

        telegramService.editKeyboard(userRequest.getChatId(), userRequest.getMessageId(), keyboard);

    }
}
