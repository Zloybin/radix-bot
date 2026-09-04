package com.arduino.telegrambot.builder.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;


public interface KeyboardBuilder {
    InlineKeyboardMarkup buildMainMenu();
    InlineKeyboardMarkup buildBackToPhysTaskMenu();

    //radTask
    InlineKeyboardMarkup buildRadConverterMenu();
    InlineKeyboardMarkup buildBackToRadConverterMenu();
    InlineKeyboardMarkup buildCompletedTaskMenu();

    //physTask
    InlineKeyboardMarkup buildPhysTaskMainMenu();
    InlineKeyboardMarkup buildPhysTaskMenu();
    InlineKeyboardMarkup buildCompletedPhysTaskWithCorrectMenu();
    InlineKeyboardMarkup buildCompletedPhysTaskMenu();
    InlineKeyboardMarkup buildInfoResultButton(boolean result);
    InlineKeyboardMarkup buildCompletedPhysTaskWithCorrectMenuWithoutAi();
    InlineKeyboardMarkup buildSettingMenu(boolean isExclude);
    InlineKeyboardMarkup buildBackToPhysTaskMenuFromStatistic();

    //handler
    InlineKeyboardMarkup buildAnkiMenu();
    InlineKeyboardMarkup buildLDecksMenu(List<String> decks);

    InlineKeyboardMarkup buildAnkiAnswerKeyboard(List<Integer> buttons);
    InlineKeyboardMarkup buildAnkiShowAnswerKeyboard();
    InlineKeyboardMarkup buildAnkiShowAnswerDuoCardsKeyboard(String word);

    InlineKeyboardMarkup buildDuoCardsMenuKeyboard();
}
