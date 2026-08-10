package com.arduino.telegrambot.builder.keyboard;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;


public interface KeyboardBuilder {
    InlineKeyboardMarkup buildMainMenu();
    InlineKeyboardMarkup buildRadConverterMenu();
    InlineKeyboardMarkup buildPhysTaskMenu();
    InlineKeyboardMarkup buildBackToMainMenu();
    InlineKeyboardMarkup buildCompletedTaskMenu();

}
