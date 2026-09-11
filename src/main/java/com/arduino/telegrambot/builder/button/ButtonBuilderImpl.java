package com.arduino.telegrambot.builder.button;

import com.arduino.telegrambot.entity.Callback;
import com.arduino.telegrambot.enummeration.AnkiAnswer;
import com.arduino.telegrambot.service.CallbackService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class ButtonBuilderImpl implements ButtonBuilder {

    @Autowired
    private CallbackService callbackService;

    @Override
    public InlineKeyboardButton buildMainMenuButton() {

        var hash = getHashWithoutRequest("start");

        var button = new InlineKeyboardButton();
        button.setText("Главное меню");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildBackToMainMenuButton() {

        var hash = getHashWithoutRequest("backToMainMenu");

        var button = new InlineKeyboardButton();
        button.setText("\uD83C\uDFE0 Гл. меню");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    //physTask

    @Override
    public InlineKeyboardButton buildPhysTaskMenuButton() {
        var hash = getHashWithoutRequest("physTaskMenu");
        var button = new InlineKeyboardButton();
        button.setText("⚛\uFE0F Задачи по физике");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildPhysTaskStartButton() {
        var hash = getHashWithoutRequest("physTask");

        var button = new InlineKeyboardButton();
        button.setText("\uD83D\uDCA1 Решить задачу");
        button.setCallbackData(String.valueOf(hash));
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

        var hash = getHashWithoutRequest("statistics");

        var button = new InlineKeyboardButton();
        button.setText("\uD83D\uDCCA Статистика");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildGiveAnswerPhysButton() {

        var hash = getHashWithoutRequest("givePhysAnswer");

        var button = new InlineKeyboardButton();
        button.setText("\uD83D\uDE4B\uD83C\uDFFB\u200D♂\uFE0F Дать ответ");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildCancelPhysTaskButton() {

        var hash = getHashWithoutRequest("cancelPhysTask");

        var button = new InlineKeyboardButton();
        button.setText("\uD83D\uDD01 Сбросить задачу");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }


    @Override
    public InlineKeyboardButton buildInfoButton(String text) {

        var hash = getHashWithoutRequest("info");

        var button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildAskAiButton() {

        var hash = getHashWithoutRequest("askAi");

        var button = new InlineKeyboardButton();
        button.setText("\uD83E\uDDE0 Спроси ИИ");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildSettingButton() {

        var hash = getHashWithoutRequest("settingPhys");

        var button = new InlineKeyboardButton();
        button.setText("🎚️Настройки");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildFilterButton() {

        var hash = getHashWithoutRequest("filterPhys");

        var button = new InlineKeyboardButton();
        button.setText("\uD83D\uDD0E Фильтр");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildExcludeCompletedTaskButton(boolean isExclude) {

        var hash = getHashWithoutRequest("excludeCompletedTask");

        String check = isExclude ? "✅" : "☑️";

        var button = new InlineKeyboardButton();
        button.setText(String.format("Искл. пройденные %s", check));
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildCorrectingResultTrueButton() {

        var hash = getHashWithoutRequest("changeToTrue");

        var button = new InlineKeyboardButton();
        button.setText("✅");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildCorrectingResultFalseButton() {

        var hash = getHashWithoutRequest("changeToFalse");

        var button = new InlineKeyboardButton();
        button.setText("❌");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildPhysTaskConfirmationButton() {

        var hash = getHashWithoutRequest("confirmTask");

        var button = new InlineKeyboardButton();
        button.setText("Продолжить");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    //radConverter

    @Override
    public InlineKeyboardButton buildRadConverterStartButton() {

        var hash = getHashWithoutRequest("radConverter");

        var button = new InlineKeyboardButton();
        button.setText("\uD83D\uDD22 Системы счисления");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildGiveAnswerButton() {

        var hash = getHashWithoutRequest("giveAnswer");

        var button = new InlineKeyboardButton();
        button.setText("Дать ответ");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildCancelTaskButton() {
        var hash = getHashWithoutRequest("cancelTask");

        var button = new InlineKeyboardButton();
        button.setText("Сбросить текущее задание");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildAnkiTaskStartButton() {

        var hash = getHashWithoutRequest("ankiMainMenu");

        var button = new InlineKeyboardButton();
        button.setText("\uD83D\uDDC3\uFE0F Карточки Anki");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildShowDecksButton() {

        var hash = getHashWithoutRequest("showDecks");

        var button = new InlineKeyboardButton();
        button.setText("🗃️Показать колодs");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildDeckNameButton(String deckName) {
        var hash = getHashWithRequest("deckName", deckName);

        var button = new InlineKeyboardButton();
        button.setText(deckName);
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildAnkiAnswerButton(AnkiAnswer ankiAnswer) {

        var hash = getHashWithRequest("ankiAnswer", String.valueOf(ankiAnswer.getIndex()));

        var button = new InlineKeyboardButton();
        button.setText(ankiAnswer.getButtonText());
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildAnswerDuocardsButton(AnkiAnswer ankiAnswer) {
        var hash = getHashWithRequest("duoCardsAnswer", String.valueOf(ankiAnswer.getIndex()));

        var button = new InlineKeyboardButton();
        button.setText(ankiAnswer.getButtonText());
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildDuoCardsButton() {

        var hash = getHashWithoutRequest("duoCards");

        var button = new InlineKeyboardButton();
        button.setText("🦉 DuoCards");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildStartDuoCardsButton() {

        var hash = getHashWithoutRequest("startDuoCards");

        var button = new InlineKeyboardButton();
        button.setText("Начать занятие");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildStartWebAppButton(String word) {
        WebAppInfo webAppInfo = new WebAppInfo();

        String url = /*"https://app.radixbot.eu.org/?word="*/"https://relating-removal-coaches-heating.trycloudflare.com/?word=" +
                URLEncoder.encode(word, StandardCharsets.UTF_8);
        webAppInfo.setUrl(url);

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Открыть в Youglish");
        button.setWebApp(webAppInfo);
        return button;
    }

    @Override
    public InlineKeyboardButton buildDeleteAnkiCardButton() {

        var hash = getHashWithoutRequest("deleteAnkiCard");

        var button = new InlineKeyboardButton();
        button.setText("⛔️ Удалить карточку");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildShowAnswerButton() {

        var hash = getHashWithoutRequest("showAnkiAnswer");

        var button = new InlineKeyboardButton();
        button.setText("Показать ответ");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }

    @Override
    public InlineKeyboardButton buildShowAnswerDuocardsButton() {
        var hash = getHashWithoutRequest("showAnswerDuocards");

        var button = new InlineKeyboardButton();
        button.setText("Показать ответ");
        button.setCallbackData(String.valueOf(hash));
        return button;
    }


    private @NonNull Long getHashWithoutRequest(String handler) {
        var callback = Callback.builder()
                .handler(handler)
                .build();
        var hash = Long.valueOf(callback.hashCode());
        callback.setId(hash);
        if (!callbackService.existBy(hash)) {
            callbackService.save(callback);
        }
        return hash;
    }

    private @NonNull Long getHashWithRequest(String handler, String request) {
        var callback = Callback.builder()
                .handler(handler)
                .request(request)
                .build();
        var hash = Long.valueOf(callback.hashCode());
        callback.setId(hash);
        if (!callbackService.existBy(hash)) {
            callbackService.save(callback);
        }
        return hash;
    }
}
