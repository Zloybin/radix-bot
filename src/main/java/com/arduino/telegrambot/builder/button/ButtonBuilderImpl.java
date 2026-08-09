package com.arduino.telegrambot.builder.button;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class ButtonBuilderImpl implements ButtonBuilder {

    @Override
    public InlineKeyboardButton buildMainMenuButton() {
        var button = new InlineKeyboardButton();
        button.setText("Главное меню");
        button.setCallbackData("/start");
        return button;
    }

    @Override
    public InlineKeyboardButton buildRadConverterStartButton() {
        var button = new InlineKeyboardButton();
        button.setText("Конверт. систем счисления");
        button.setCallbackData("radConverter");
        return button;
    }

    @Override
    public InlineKeyboardButton buildGiveAnswerButton() {
        var button = new InlineKeyboardButton();
        button.setText("Дать ответ");
        button.setCallbackData("giveAnswer");
        return button;
    }

    @Override
    public InlineKeyboardButton buildCancelTaskButton() {
        var button = new InlineKeyboardButton();
        button.setText("Сбросить текущее задание");
        button.setCallbackData("cancelTask");
        return button;
    }

    @Override
    public InlineKeyboardButton buildNewTaskButton() {
        var button = new InlineKeyboardButton();
        button.setText("Новое задание");
        button.setCallbackData("radConverter");
        return button;
    }

    @Override
    public InlineKeyboardButton buildNewPhysTaskButton() {
        var button = new InlineKeyboardButton();
        button.setText("Новая задача по физике");
        button.setCallbackData("physTask");
        return button;
    }
}
