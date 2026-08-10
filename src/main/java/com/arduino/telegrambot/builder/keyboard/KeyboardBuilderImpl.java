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

        var radConverterButton = buttonBuilder.buildRadConverterStartButton();
        var physTaskButton = buttonBuilder.buildNewPhysTaskButton();

        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(radConverterButton);

        var row2 = new ArrayList<InlineKeyboardButton>();
        row2.add(physTaskButton);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
        rows.add(row2);
        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup buildRadConverterMenu() {

        var accept = buttonBuilder.buildGiveAnswerButton();
        var cancel = buttonBuilder.buildCancelTaskButton();
        var main = buttonBuilder.buildMainMenuButton();

        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(accept);

        var row2 = new ArrayList<InlineKeyboardButton>();
        row2.add(cancel);

        var row3 = new ArrayList<InlineKeyboardButton>();
        row3.add(main);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup buildPhysTaskMenu() {
        var accept = buttonBuilder.buildGiveAnswerPhysButton();
        var cancel = buttonBuilder.buildCancelTaskPhysButton();
        var main = buttonBuilder.buildMainMenuButton();

        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(accept);

        var row2 = new ArrayList<InlineKeyboardButton>();
        row2.add(cancel);

        var row3 = new ArrayList<InlineKeyboardButton>();
        row3.add(main);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup buildBackToMainMenu() {
        var main = buttonBuilder.buildMainMenuButton();

        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(main);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);

        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup buildCompletedTaskMenu() {
        var newTask = buttonBuilder.buildNewTaskButton();
        var main = buttonBuilder.buildMainMenuButton();

        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(newTask);

        var row2 = new ArrayList<InlineKeyboardButton>();
        row2.add(main);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
        rows.add(row2);

        return new InlineKeyboardMarkup(rows);
    }



}
