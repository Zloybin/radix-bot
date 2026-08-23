package com.arduino.telegrambot.builder.keyboard;

import com.arduino.telegrambot.builder.button.ButtonBuilder;
import com.arduino.telegrambot.builder.button.procesor.ButtonProcessor;
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

    @Autowired
    private ButtonProcessor buttonProcessor;

    @Override
    public InlineKeyboardMarkup buildMainMenu() {

        var radConverterButton = buttonBuilder.buildRadConverterStartButton();
        var physTaskButton = buttonBuilder.buildPhysTaskMenuButton();
        var ankiTaskButton = buttonBuilder.buildAnkiTaskStartButton();

        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(radConverterButton);

        var row2 = new ArrayList<InlineKeyboardButton>();
        row2.add(physTaskButton);

        var row3 = new ArrayList<InlineKeyboardButton>();
        row3.add(ankiTaskButton);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup buildBackToPhysTaskMenu() {
        var taskMenu = buttonBuilder.buildPhysTaskStartButton();

        var cancelAnswer = buttonProcessor.renameButton(taskMenu, "\uD83D\uDEAB Отменить ответ");

        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(cancelAnswer);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);

        return new InlineKeyboardMarkup(rows);
    }

    //radTask

    @Override
    public InlineKeyboardMarkup buildRadConverterMenu() {

        var accept = buttonBuilder.buildGiveAnswerButton();
        var cancel = buttonBuilder.buildCancelTaskButton();
        var main = buttonBuilder.buildBackToMainMenuButton();

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
    public InlineKeyboardMarkup buildCompletedTaskMenu() {
        var radTaskButton = buttonBuilder.buildRadConverterStartButton();
        var newRadTaskButton = buttonProcessor.renameButton(radTaskButton, "Новое задание по сист. счисления");
        var main = buttonBuilder.buildMainMenuButton();

        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(newRadTaskButton);

        var row2 = new ArrayList<InlineKeyboardButton>();
        row2.add(main);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
        rows.add(row2);

        return new InlineKeyboardMarkup(rows);
    }



    //physTask

    @Override
    public InlineKeyboardMarkup buildPhysTaskMainMenu() {
        var newTask = buttonBuilder.buildPhysTaskStartButton();
        var statistics = buttonBuilder.buildStatisticsButton();
        var main = buttonBuilder.buildBackToMainMenuButton();

        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(newTask);

        var row2 = new ArrayList<InlineKeyboardButton>();
        row2.add(statistics);

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
        var cancel = buttonBuilder.buildCancelPhysTaskButton();
        var openSource = buttonBuilder.buildOpenSourceFileButton();
        var back = buttonBuilder.buildPhysTaskMenuButton();

        var renamedBack  = buttonProcessor.renameButton(back, "⬅\uFE0F Назад");


        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(accept);

        var row2 = new ArrayList<InlineKeyboardButton>();
        row2.add(cancel);

        var row3 = new ArrayList<InlineKeyboardButton>();
        row3.add(renamedBack);
        row3.add(openSource);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);

        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup buildCompletedPhysTaskWithCorrectMenu() {

        var correctTrue = buttonBuilder.buildCorrectingResultTrueButton();
        var correctFalse = buttonBuilder.buildCorrectingResultFalseButton();
        var confirmButton = buttonBuilder.buildPhysTaskConfirmationButton();

        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(confirmButton);

        var row2 = new ArrayList<InlineKeyboardButton>();
        row2.add(correctTrue);
        row2.add(correctFalse);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
        rows.add(row2);

        return new InlineKeyboardMarkup(rows);

    }

    @Override
    public InlineKeyboardMarkup buildCompletedPhysTaskMenu() {
        var newTask = buttonBuilder.buildPhysTaskStartButton();
        var backToPhysTaskMenu = buttonBuilder.buildPhysTaskMenuButton();
        var renamedBackButton = buttonProcessor.renameButton(backToPhysTaskMenu, "Осн. меню");

        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(newTask);

        var row2 = new ArrayList<InlineKeyboardButton>();
        row2.add(renamedBackButton);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
        rows.add(row2);

        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup buildInfoResultButton(boolean result) {

        var text = result ? "\uD83D\uDFE9 Успешно выполнено \uD83D\uDFE9" : "\uD83D\uDFE5 Провалено \uD83D\uDFE5";
        var infoButton = buttonBuilder.buildInfoButton(text);

        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(infoButton);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);

        return new InlineKeyboardMarkup(rows);
    }


}
