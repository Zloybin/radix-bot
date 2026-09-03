package com.arduino.telegrambot.builder.button;

import com.arduino.telegrambot.enummeration.AnkiAnswer;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Locale;

@Component
public class ButtonBuilderImpl implements ButtonBuilder {

    @Override
    public InlineKeyboardButton buildMainMenuButton() {
        var button = new InlineKeyboardButton();
        button.setText("Главное меню");
        button.setCallbackData("start");
        return button;
    }

    @Override
    public InlineKeyboardButton buildBackToMainMenuButton() {
        var button = new InlineKeyboardButton();
        button.setText("\uD83C\uDFE0 Гл. меню");
        button.setCallbackData("backToMainMenu");
        return button;
    }

    //physTask

    @Override
    public InlineKeyboardButton buildPhysTaskMenuButton() {
        var button = new InlineKeyboardButton();
        button.setText("⚛\uFE0F Задачи по физике");
        button.setCallbackData("physTaskMenu");
        return button;
    }

    @Override
    public InlineKeyboardButton buildPhysTaskStartButton() {
        var button = new InlineKeyboardButton();
        button.setText("\uD83D\uDCA1 Решить задачу");
        button.setCallbackData("physTask");
        return button;
    }

    @Override
    public InlineKeyboardButton buildOpenSourceFileButton() {
        var button = new InlineKeyboardButton();
        button.setText("\uD83D\uDCD6");
        button.setUrl("https://drive.google.com/file/d/1xJ_ywA8fZktF9U_N-iDKmL5wavw-GlDV/view?usp=sharing");
        return button;
    }

    @Override
    public InlineKeyboardButton buildStatisticsButton() {
        var button = new InlineKeyboardButton();
        button.setText("\uD83D\uDCCA Статистика");
        button.setCallbackData("statistics");
        return button;
    }

    @Override
    public InlineKeyboardButton buildGiveAnswerPhysButton() {
        var button = new InlineKeyboardButton();
        button.setText("\uD83D\uDE4B\uD83C\uDFFB\u200D♂\uFE0F Дать ответ");
        button.setCallbackData("givePhysAnswer");
        return button;
    }

    @Override
    public InlineKeyboardButton buildCancelPhysTaskButton() {
        var button = new InlineKeyboardButton();
        button.setText("\uD83D\uDD01 Сбросить задачу");
        button.setCallbackData("cancelPhysTask");
        return button;
    }



    @Override
    public InlineKeyboardButton buildInfoButton(String text) {
        var button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData("info");
        return button;
    }

    @Override
    public InlineKeyboardButton buildAskAiButton() {
        var button = new InlineKeyboardButton();
        button.setText("\uD83E\uDDE0 Спроси ИИ");
        button.setCallbackData("askAi");
        return button;
    }

    @Override
    public InlineKeyboardButton buildSettingButton() {
        var button = new InlineKeyboardButton();
        button.setText("🎚️Настройки");
        button.setCallbackData("settingPhys");
        return button;
    }

    @Override
    public InlineKeyboardButton buildFilterButton() {
        var button = new InlineKeyboardButton();
        button.setText("\uD83D\uDD0E Фильтр");
        button.setCallbackData("filterPhys");
        return button;
    }

    @Override
    public InlineKeyboardButton buildExcludeCompletedTaskButton(boolean isExclude) {
        String check = isExclude ? "✅" : "☑️";

        var button = new InlineKeyboardButton();
        button.setText(String.format("Искл. пройденные %s", check));
        button.setCallbackData("excludeCompletedTask");
        return button;
    }

    @Override
    public InlineKeyboardButton buildCorrectingResultTrueButton() {
        var button = new InlineKeyboardButton();
        button.setText("✅");
        button.setCallbackData("changeToTrue");
        return button;
    }

    @Override
    public InlineKeyboardButton buildCorrectingResultFalseButton() {
        var button = new InlineKeyboardButton();
        button.setText("❌");
        button.setCallbackData("changeToFalse");
        return button;
    }

    @Override
    public InlineKeyboardButton buildPhysTaskConfirmationButton() {
        var button = new InlineKeyboardButton();
        button.setText("Продолжить");
        button.setCallbackData("confirmTask");
        return button;
    }

    //radConverter

    @Override
    public InlineKeyboardButton buildRadConverterStartButton() {
        var button = new InlineKeyboardButton();
        button.setText("\uD83D\uDD22 Системы счисления");
        button.setCallbackData("radConverter");
        return button;
    }

    @Override
    public InlineKeyboardButton buildGiveAnswerButton() {
        var button = new InlineKeyboardButton();
        button.setText("Дать ответ");
        button.setCallbackData("giveAnswer");
        return button;
    }

    @Override
    public InlineKeyboardButton buildCancelTaskButton() {
        var button = new InlineKeyboardButton();
        button.setText("Сбросить текущее задание");
        button.setCallbackData("cancelTask");
        return button;
    }

    @Override
    public InlineKeyboardButton buildAnkiTaskStartButton() {
        var button = new InlineKeyboardButton();
        button.setText("\uD83D\uDDC3\uFE0FКарточки Anki");
        button.setCallbackData("handler");
        return button;
    }

    @Override
    public InlineKeyboardButton buildShowDecksButton() {
        var button = new InlineKeyboardButton();
        button.setText("🗃️Показать колод");
        button.setCallbackData("showDecks");
        return button;
    }

    @Override
    public InlineKeyboardButton buildDeckNameButton(String deckName) {
        var button = new InlineKeyboardButton();
        button.setText(deckName);
        button.setCallbackData(deckName.toLowerCase());
        return button;
    }

    @Override
    public InlineKeyboardButton buildAnkiAnswerButton(AnkiAnswer ankiAnswer) {
        var button = new InlineKeyboardButton();
        button.setText(ankiAnswer.name());
        button.setCallbackData(String.valueOf(ankiAnswer.getIndex()));
        return button;
    }

    @Override
    public InlineKeyboardButton buildShowAnswerButton() {
        var button = new InlineKeyboardButton();
        button.setText("Показать ответ");
        button.setCallbackData("showAnkiAnswer");
        return button;
    }
}
