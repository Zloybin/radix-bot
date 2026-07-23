package com.arduino.telegrambot.builder.button;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class ButtonBuilderImpl implements ButtonBuilder {
    @Override
    public InlineKeyboardButton buildRadConverterStartButton() {

        var button = new InlineKeyboardButton();
        button.setText("Конвертер систем счисления");
        button.setCallbackData("radConverter");
        return button;

    }

    @Override
    public InlineKeyboardButton buildMainMenuButton() {
        var button = new InlineKeyboardButton();
        button.setText("Главное меню");
        button.setCallbackData("/start");
        return button;
    }
}
