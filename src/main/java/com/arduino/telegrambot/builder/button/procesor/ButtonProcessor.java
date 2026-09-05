package com.arduino.telegrambot.builder.button.procesor;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public interface ButtonProcessor {
    InlineKeyboardButton renameButton(InlineKeyboardButton button, String newText);
}
