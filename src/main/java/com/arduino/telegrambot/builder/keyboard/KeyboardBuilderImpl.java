package com.arduino.telegrambot.builder.keyboard;

import com.arduino.telegrambot.builder.button.ButtonBuilder;
import com.arduino.telegrambot.builder.button.procesor.ButtonProcessor;
import com.arduino.telegrambot.enummeration.AnkiAnswer;
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
        var duoCards = buttonBuilder.buildDuoCardsButton();

        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(radConverterButton);

        var row2 = new ArrayList<InlineKeyboardButton>();
        row2.add(physTaskButton);

        var row3 = new ArrayList<InlineKeyboardButton>();
        row3.add(ankiTaskButton);

        var row4 = new ArrayList<InlineKeyboardButton>();
        row4.add(duoCards);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
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

    @Override
    public InlineKeyboardMarkup buildAnkiMenu() {
        var showDecks = buttonBuilder.buildShowDecksButton();
        var backToMainMenu = buttonBuilder.buildBackToMainMenuButton();

        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(showDecks);

        var row2 = new ArrayList<InlineKeyboardButton>();
        row2.add(backToMainMenu);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
        rows.add(row2);

        return new InlineKeyboardMarkup(rows);

    }

    @Override
    public InlineKeyboardMarkup buildLDecksMenu(List<String> decks) {

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        for (String deck : decks) {
            var row = new ArrayList<InlineKeyboardButton>();
            var deckNameButton = buttonBuilder.buildDeckNameButton(deck);
            row.add(deckNameButton);
            rows.add(row);
        }


        var ankiMenu = buttonBuilder.buildAnkiTaskStartButton();
        var backToAnkiStartMenu = buttonProcessor.renameButton(ankiMenu, "⬅️ Назад");
        var ankiRow = new ArrayList<InlineKeyboardButton>();
        ankiRow.add(backToAnkiStartMenu);
        rows.add(ankiRow);

        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup buildAnkiAnswerKeyboard(List<Integer> buttonIndexes) {

        var rows = new ArrayList<List<InlineKeyboardButton>>();

        for (Integer buttonIndex : buttonIndexes) {
            InlineKeyboardButton answerButton;
            for (AnkiAnswer ankiAnswer : AnkiAnswer.values()) {
                if (ankiAnswer.getIndex() == buttonIndex) {
                    answerButton = buttonBuilder.buildAnkiAnswerButton(ankiAnswer);
                    var row = new ArrayList<InlineKeyboardButton>();
                    row.add(answerButton);
                    rows.add(row);
                    break;
                }
            }
        }

        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup buildAnkiAnswerDuoCardsKeyboard(List<Integer> buttonIndexes, String word) {
        var rows = new ArrayList<List<InlineKeyboardButton>>();

        var startWebAppButton = buttonBuilder.buildStartWebAppButton(word);
        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(startWebAppButton);
        var row2 = new ArrayList<InlineKeyboardButton>();
        var row3 = new ArrayList<InlineKeyboardButton>();


        for (Integer buttonIndex : buttonIndexes) {
            InlineKeyboardButton answerButton;
            for (AnkiAnswer ankiAnswer : AnkiAnswer.values()) {
                if (ankiAnswer.getIndex() == buttonIndex) {
                    List<InlineKeyboardButton> row;
                    if(buttonIndex <= 2){
                        row = row2;
                    }else{
                        row = row3;
                    }
                    answerButton = buttonBuilder.buildAnkiAnswerButton(ankiAnswer);
                    row.add(answerButton);
                    break;
                }
            }
        }
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);

        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup buildAnkiShowAnswerKeyboard() {

        var showAnswerButton = buttonBuilder.buildShowAnswerButton();
        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(showAnswerButton);

        var showDecksButton = buttonBuilder.buildShowDecksButton();
        var backToShowDeckNames = buttonProcessor.renameButton(showDecksButton, "⬅️ Назад");
        var row2 = new ArrayList<InlineKeyboardButton>();
        row2.add(backToShowDeckNames);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
        rows.add(row2);

        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup buildAnkiShowAnswerDuoCardsKeyboard(String word) {

        var showAnswerButton = buttonBuilder.buildShowAnswerButton();
        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(showAnswerButton);

        var startWebAppButton = buttonBuilder.buildStartWebAppButton(word);
        var row2 = new ArrayList<InlineKeyboardButton>();
        row2.add(startWebAppButton);

        var showDecksButton = buttonBuilder.buildDuoCardsButton();
        var backToShowDeckNames = buttonProcessor.renameButton(showDecksButton, "⬅️ Назад");
        var row3 = new ArrayList<InlineKeyboardButton>();
        row3.add(backToShowDeckNames);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);

        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup buildDuoCardsMenuKeyboard() {
        var startDuoCards = buttonBuilder.buildStartDuoCardsButton();
        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(startDuoCards);

        var showDecksButton = buttonBuilder.buildMainMenuButton();
        var backToShowDeckNames = buttonProcessor.renameButton(showDecksButton, "⬅️ Назад");
        var row2 = new ArrayList<InlineKeyboardButton>();
        row2.add(backToShowDeckNames);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);
        rows.add(row2);

        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup buildBackToDuoCardsMenuKeyboard() {
        var duoCards = buttonBuilder.buildDuoCardsButton();
        buttonProcessor.renameButton(duoCards, "⬅️ Назад");
        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(duoCards);

        var rows = new ArrayList<List<InlineKeyboardButton>>();
        rows.add(row1);

        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardMarkup buildBackToAnkiDecksMenu() {
        var duoCards = buttonBuilder.buildShowDecksButton();
        buttonProcessor.renameButton(duoCards, "⬅️ Назад");
        var row1 = new ArrayList<InlineKeyboardButton>();
        row1.add(duoCards);

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
