package com.arduino.telegrambot.ui.template;

import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.feature.anki.model.AnkiDeckStats;
import com.arduino.telegrambot.feature.physik.model.SectionProgress;

import java.util.List;

public interface TemplateProcessor {
    String processGreetingsTemplate();
    String processUserProfileTemplate(String userName, long tasksCount, long completedTaskCount);
    String processRadTaskTemplate(String source, String target, String task);
    String processPhysTaskTemplate(String section, String title, long taskNumber, int selfNumber, String taskLevel, String taskText, int pageNumber);
    String processPhysTaskWaitUserAnswerTemplate();
    String processPhysTaskWithAiTemplate(String title, long taskNumber, int selfNumber, String taskLevel, String taskText, int pageNumber, String section, String aiAnswer, String userAnswer, String rightAnswer);
    String processPhysTaskWaitAiTemplate(String title, long taskNumber, int selfNumber, String taskLevel, String taskText, int pageNumber, String section, String userAnswer, String rightAnswer);
    String processSuccessCorrectTemplate(long taskId, boolean result);
    String processConfirmPhysTaskTemplate();
    String processPhysUserResultMessageTemplate(boolean result, String rightAnswer, String userAnswer, String section, String title, long taskNumber, int selfNumber, String taskLevel, String taskText, int pageNumber);
    String processRadUserResultMessageTemplate(boolean result, String rightAnswer, String userAnswer, String source, String target, String task);
    String processStatisticTemplate(List<SectionProgress> sectionProgresses);
    String processAnkiUserProfileTemplate(int numCardsReviewedToday);
    String processDuoCardsUserProfileTemplate();
    String processDecksMenuTemplate();
    String processFrontCardTemplate(AnkiCurrentCard currentCard, AnkiDeckStats ankiDeckStats);
    String processFrontCardDuoCardsTemplate(AnkiCurrentCard currentCard, AnkiDeckStats ankiDeckStats);
    String processBackCardTemplate(AnkiCurrentCard currentCard,  AnkiDeckStats ankiDeckStats);
    String processBackCardDuoCardsTemplate(AnkiCurrentCard currentCard,  AnkiDeckStats ankiDeckStats);

    String processCompletedDeckTemplate(String deckname);

    String processAppsInfoTemplate();
}
