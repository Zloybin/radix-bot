package com.arduino.telegrambot.builder.keyboard;

import com.arduino.telegrambot.feature.anki.model.AnkiDeckStats;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;
import java.util.Map;


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
    InlineKeyboardMarkup buildAnkiMenu();
    InlineKeyboardMarkup buildDecksMenu(List<String> decks, Map<String, AnkiDeckStats> stats);

    InlineKeyboardMarkup buildAnkiAnswerKeyboard(List<Integer> buttons);
    InlineKeyboardMarkup buildAnkiAnswerDuoCardsKeyboard(List<Integer> buttons, String word);
    InlineKeyboardMarkup buildAnkiCardKeyboard();
    InlineKeyboardMarkup buildAnkiShowAnswerDuoCardsKeyboard(String word);

    InlineKeyboardMarkup buildDuoCardsMenuKeyboard();

    InlineKeyboardMarkup buildBackToDuoCardsMenuKeyboard();

    InlineKeyboardMarkup buildBackToAnkiDecksMenu();
    ReplyKeyboard buildForcedKeyboardMenu(String text);

    InlineKeyboardMarkup buildAppsInfoMenu();

    InlineKeyboardMarkup buildConfirmDeleteCardKeyboard();
}
