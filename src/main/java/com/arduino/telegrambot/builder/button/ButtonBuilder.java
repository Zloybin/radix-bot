package com.arduino.telegrambot.builder.button;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public interface ButtonBuilder {

    InlineKeyboardButton buildMainMenuButton();
    InlineKeyboardButton buildBackToMainMenuButton();


    //physTask
    InlineKeyboardButton buildPhysTaskMenuButton();
    InlineKeyboardButton buildPhysTaskStartButton();
    InlineKeyboardButton buildOpenSourceFileButton();
    InlineKeyboardButton buildGiveAnswerPhysButton();
    InlineKeyboardButton buildCancelPhysTaskButton();
    InlineKeyboardButton buildCorrectingResultTrueButton();
    InlineKeyboardButton buildCorrectingResultFalseButton();
    InlineKeyboardButton buildPhysTaskConfirmationButton();


    //radConverter
    InlineKeyboardButton buildRadConverterStartButton();
    InlineKeyboardButton buildGiveAnswerButton();
    InlineKeyboardButton buildCancelTaskButton();

}
