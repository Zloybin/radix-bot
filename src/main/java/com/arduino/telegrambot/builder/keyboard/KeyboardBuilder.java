package com.arduino.telegrambot.builder.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;


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
}
