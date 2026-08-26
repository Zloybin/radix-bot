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

    @Override
    public InlineKeyboardMarkup buildBackToPhysTaskMenuFromStatistic() {
        var taskMenu = buttonBuilder.buildPhysTaskMenuButton();

        var cancelAnswer = buttonProcessor.renameButton(taskMenu, "⬅️ Назад");

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
    public InlineKeyboardMarkup buildBackToRadConverterMenu() {
        var radConverterButton = buttonBuilder.buildRadConverterStartButton();
        var cancelAnswer = buttonProcessor.renameButton(radConverterButton, "\uD83D\uDEAB Отменить ответ");

        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(cancelAnswer);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);

        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup buildCompletedTaskMenu() {
        var radTaskButton = buttonBuilder.buildRadConverterStartButton();
        var newRadTaskButton = buttonProcessor.renameButton(radTaskButton, "Новое задание");
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
        var setting = buttonBuilder.buildSettingButton();
        var statistics = buttonBuilder.buildStatisticsButton();
        var main = buttonBuilder.buildBackToMainMenuButton();

        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(newTask);

        var row2 = new ArrayList<InlineKeyboardButton>();
        row2.add(setting);

        var row3 = new ArrayList<InlineKeyboardButton>();
        row3.add(statistics);

        var row4 = new ArrayList<InlineKeyboardButton>();
        row4.add(main);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);

        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup buildPhysTaskMenu() {
        var accept = buttonBuilder.buildGiveAnswerPhysButton();
//        var askAi = buttonBuilder.buildAskAiButton();
        var cancel = buttonBuilder.buildCancelPhysTaskButton();
        var openSource = buttonBuilder.buildOpenSourceFileButton();
        var back = buttonBuilder.buildPhysTaskMenuButton();

        var renamedBack = buttonProcessor.renameButton(back, "⬅\uFE0F Назад");


        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(accept);

//        var row2 = new ArrayList<InlineKeyboardButton>();
//        row2.add(askAi);

        var row3 = new ArrayList<InlineKeyboardButton>();
        row3.add(cancel);

        var row4 = new ArrayList<InlineKeyboardButton>();
        row4.add(renamedBack);
        row4.add(openSource);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
//        rows.add(row2);
        rows.add(row3);
        rows.add(row4);

        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup buildCompletedPhysTaskWithCorrectMenu() {

        var correctTrue = buttonBuilder.buildCorrectingResultTrueButton();
        var correctFalse = buttonBuilder.buildCorrectingResultFalseButton();
        var askAi = buttonBuilder.buildAskAiButton();
//        var confirmButton = buttonBuilder.buildPhysTaskConfirmationButton();

        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(correctTrue);
        row1.add(correctFalse);

//        var row2 = new ArrayList<InlineKeyboardButton>();
//        row2.add(confirmButton);

        var row2 = new ArrayList<InlineKeyboardButton>();
        row2.add(askAi);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
//        rows.add(row2);
        rows.add(row2);

        return new InlineKeyboardMarkup(rows);

    }

    @Override
    public InlineKeyboardMarkup buildCompletedPhysTaskWithCorrectMenuWithoutAi() {

        var correctTrue = buttonBuilder.buildCorrectingResultTrueButton();
        var correctFalse = buttonBuilder.buildCorrectingResultFalseButton();
//        var askAi = buttonBuilder.buildAskAiButton();


        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(correctTrue);
        row1.add(correctFalse);

//        var row2 = new ArrayList<InlineKeyboardButton>();
//        row2.add(askAi);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
//        rows.add(row2);

        return new InlineKeyboardMarkup(rows);

    }

    @Override
    public InlineKeyboardMarkup buildSettingMenu(boolean isExclude) {
        var exclude = buttonBuilder.buildExcludeCompletedTaskButton(isExclude);
        var filter = buttonBuilder.buildFilterButton();
        var physMenu = buttonBuilder.buildPhysTaskMenuButton();
        var backToPhysMenu = buttonProcessor.renameButton(physMenu, "⬅️ Назад");

        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(exclude);

        var row2 = new ArrayList<InlineKeyboardButton>();
        row2.add(filter);


        var row3 = new ArrayList<InlineKeyboardButton>();
        row3.add(backToPhysMenu);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);

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
