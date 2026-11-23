package com.arduino.telegrambot.builder.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;


public interface KeyboardBuilder {
    InlineKeyboardMarkup buildMainMenu();

    //radTask
    InlineKeyboardMarkup buildRadConverterTaskMenu();
    InlineKeyboardMarkup buildBackToRadConverterMenu();
    InlineKeyboardMarkup buildCompletedTaskMenu();

    //physTask
    InlineKeyboardMarkup buildPhysTaskMainMenu();
    InlineKeyboardMarkup buildSettingMenu(boolean isExclude);
    InlineKeyboardMarkup buildWaitingForAnswerMenu();
    InlineKeyboardMarkup buildBackToPhysTaskMenuFromStatistic();

    InlineKeyboardMarkup buildPhysTaskCardMenu();
    InlineKeyboardMarkup buildCompletedPhysTaskWithCorrectMenu();
    InlineKeyboardMarkup buildCompletedPhysTaskWithCorrectMenuWithoutAi();
    InlineKeyboardMarkup buildCompletedPhysTaskMenu();
    InlineKeyboardMarkup buildInfoResultButton(boolean result);

    //anki
    InlineKeyboardMarkup buildAnkiMenu(List<String> decks);
    InlineKeyboardMarkup buildAnkiCardKeyboard();
    InlineKeyboardMarkup buildAnkiAnswerKeyboard(List<Integer> buttons);
    InlineKeyboardMarkup buildBackToAnkiDecksMenu();


    InlineKeyboardMarkup buildDuoCardsMenuKeyboard();
    InlineKeyboardMarkup buildAnkiShowAnswerDuoCardsKeyboard(String word);
    InlineKeyboardMarkup buildAnkiAnswerDuoCardsKeyboard(List<Integer> buttons, String word);
    InlineKeyboardMarkup buildBackToDuoCardsMenuKeyboard();






    InlineKeyboardMarkup buildAppsInfoMenu();

    InlineKeyboardMarkup buildConfirmDeleteCardKeyboard();

    InlineKeyboardMarkup buildCancelAddNewNoteKeyboard();

    InlineKeyboardMarkup buildProcessNewDuoCardKeyboard();

    InlineKeyboardMarkup buildPreviewSaveDuoCardKeyboard();

    InlineKeyboardMarkup buildExerciseSentenceKeyboard();

    InlineKeyboardMarkup buildCancelSentenceExerciseKeyboard();

    InlineKeyboardMarkup buildResultSentenceExerciseKeyboard();

    InlineKeyboardMarkup buildCancelSaveMediaKeyboard();

    InlineKeyboardMarkup buildAdminMenuKeyboard();

    InlineKeyboardMarkup buildBackToAdminMenu();

    InlineKeyboardMarkup buildAdditionalTaskMenuKeyboard();
}
