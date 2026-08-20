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
    public InlineKeyboardButton buildBackToMainMenuButton() {
        var button = new InlineKeyboardButton();
        button.setText("Назад в главное меню");
        button.setCallbackData("backToMainMenu");
        return button;
    }

    //physTask

    @Override
    public InlineKeyboardButton buildPhysTaskStartButton() {
        var button = new InlineKeyboardButton();
        button.setText("Задачи по физике");
        button.setCallbackData("physTask");
        return button;
    }

    @Override
    public InlineKeyboardButton buildGiveAnswerPhysButton() {
        var button = new InlineKeyboardButton();
        button.setText("Дать ответ");
        button.setCallbackData("givePhysAnswer");
        return button;
    }

    @Override
    public InlineKeyboardButton buildCancelPhysTaskButton() {
        var button = new InlineKeyboardButton();
        button.setText("Сбросить текущее задание");
        button.setCallbackData("cancelPhysTask");
        return button;
    }

    @Override
    public InlineKeyboardButton buildCorrectingResultTrueButton() {
        var button = new InlineKeyboardButton();
        button.setText("✅");
        button.setCallbackData("changeToTrue");
        return button;
    }

    @Override
    public InlineKeyboardButton buildCorrectingResultFalseButton() {
        var button = new InlineKeyboardButton();
        button.setText("❌");
        button.setCallbackData("changeToFalse");
        return button;
    }

    @Override
    public InlineKeyboardButton buildPhysTaskConfirmationButton() {
        var button = new InlineKeyboardButton();
        button.setText("Подтвердить прохождение упражнения");
        button.setCallbackData("confirmTask");
        return button;
    }

    //radConverter

    @Override
    public InlineKeyboardButton buildRadConverterStartButton() {
        var button = new InlineKeyboardButton();
        button.setText("Задачи по системам счисления");
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
}
