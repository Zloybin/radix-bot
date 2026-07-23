package com.arduino.telegrambot.builder.keyboard;

import com.arduino.telegrambot.builder.button.ButtonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardBuilderImpl implements KeyboardBuilder {

    @Autowired
    private ButtonBuilder buttonBuilder;

    @Override
    public InlineKeyboardMarkup buildMainMenu() {

        var button = buttonBuilder.buildRadConverterStartButton();

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        var row = new ArrayList<InlineKeyboardButton>();
        row.add(button);
        rows.add(row);
        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup buildRadConverterMenu() {

        var button = buttonBuilder.buildMainMenuButton();

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        var row = new ArrayList<InlineKeyboardButton>();
        row.add(button);
        rows.add(row);
        return new InlineKeyboardMarkup(rows);
    }

}
