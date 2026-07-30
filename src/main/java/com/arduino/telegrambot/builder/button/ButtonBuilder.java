package com.arduino.telegrambot.builder.button;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public interface ButtonBuilder {

    InlineKeyboardButton buildRadConverterStartButton();
    InlineKeyboardButton buildGiveAnswerButton();
    InlineKeyboardButton buildCancelTaskButton();
    InlineKeyboardButton buildMainMenuButton();
    InlineKeyboardButton buildNewTaskButton();
}
