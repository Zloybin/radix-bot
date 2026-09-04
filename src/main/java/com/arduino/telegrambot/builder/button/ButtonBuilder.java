package com.arduino.telegrambot.builder.button;

import com.arduino.telegrambot.enummeration.AnkiAnswer;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public interface ButtonBuilder {

    InlineKeyboardButton buildMainMenuButton();
    InlineKeyboardButton buildBackToMainMenuButton();


    //physTask
    InlineKeyboardButton buildPhysTaskMenuButton();
    InlineKeyboardButton buildPhysTaskStartButton();
    InlineKeyboardButton buildOpenSourceFileButton();
    InlineKeyboardButton buildStatisticsButton();
    InlineKeyboardButton buildGiveAnswerPhysButton();
    InlineKeyboardButton buildCancelPhysTaskButton();
    InlineKeyboardButton buildCorrectingResultTrueButton();
    InlineKeyboardButton buildCorrectingResultFalseButton();
    InlineKeyboardButton buildPhysTaskConfirmationButton();
    InlineKeyboardButton buildInfoButton(String text);
    InlineKeyboardButton buildAskAiButton();
    InlineKeyboardButton buildSettingButton();
    InlineKeyboardButton buildFilterButton();
    InlineKeyboardButton buildExcludeCompletedTaskButton(boolean isExclude);


    //radConverter
    InlineKeyboardButton buildRadConverterStartButton();
    InlineKeyboardButton buildGiveAnswerButton();
    InlineKeyboardButton buildCancelTaskButton();

    //handler
    InlineKeyboardButton buildAnkiTaskStartButton();
    InlineKeyboardButton buildShowDecksButton();
    InlineKeyboardButton buildShowAnswerButton();
    InlineKeyboardButton buildDeckNameButton(String deckName);
    InlineKeyboardButton buildAnkiAnswerButton(AnkiAnswer ankiAnswer);

    InlineKeyboardButton buildDuoCardsButton();

    InlineKeyboardButton buildStartDuoCardsButton();
}
