package com.arduino.telegrambot.builder.button;

import com.arduino.telegrambot.enummeration.AnkiAnswer;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public interface ButtonBuilder {

    InlineKeyboardButton buildMainMenuButton();
    InlineKeyboardButton buildAppsInfoButton();
    InlineKeyboardButton buildBackToMainMenuButton();

    //physTask
    InlineKeyboardButton buildPhysTaskMenuButton();
    InlineKeyboardButton buildStatisticsButton();

    InlineKeyboardButton buildSettingButton();
    InlineKeyboardButton buildExcludeCompletedTaskButton(boolean isExclude);
    InlineKeyboardButton buildFilterButton();

    InlineKeyboardButton buildPhysTaskStartButton();

    InlineKeyboardButton buildGiveAnswerPhysButton();
    InlineKeyboardButton buildCancelPhysTaskButton();
    InlineKeyboardButton buildCancelPhysAnswerButton();
    InlineKeyboardButton buildOpenBookButton();

    InlineKeyboardButton buildCorrectingResultTrueButton();
    InlineKeyboardButton buildCorrectingResultFalseButton();
    InlineKeyboardButton buildAskAiButton();
    InlineKeyboardButton buildInfoButton(String text);

    //radConverter
    InlineKeyboardButton buildRadConverterStartButton();
    InlineKeyboardButton buildGiveAnswerButton();
    InlineKeyboardButton buildCancelTaskButton();
    InlineKeyboardButton buildCancelAnswerButton();

    //Anki
    InlineKeyboardButton buildAnkiMenuButton();
    InlineKeyboardButton buildShowDecksButton();
    InlineKeyboardButton buildDeckNameButton(String deckName);
    InlineKeyboardButton buildShowAnkiAnswerButton();
    InlineKeyboardButton buildDeleteAnkiCardButton();
    InlineKeyboardButton buildAnkiOptionAnswerButton(AnkiAnswer ankiAnswer);
    InlineKeyboardButton buildCancelDeleteCardButton();
    InlineKeyboardButton buildConfirmDeleteCardButton();
    InlineKeyboardButton buildMarkAnkiCardButton();

    //DuoCard
    InlineKeyboardButton buildDuoCardsMenuButton();
    InlineKeyboardButton buildStartDuoCardsButton();
    InlineKeyboardButton buildYouglishStartButton(String word);
    InlineKeyboardButton buildShowAnswerDuoCardsButton();
    InlineKeyboardButton buildAnswerOptionDuoCardsButton(AnkiAnswer ankiAnswer);


}
