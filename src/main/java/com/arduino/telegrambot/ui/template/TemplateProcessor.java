package com.arduino.telegrambot.ui.template;

import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.feature.anki.model.AnkiMenuDeckInfo;
import com.arduino.telegrambot.feature.anki.model.raw.AnkiDeckStats;
import com.arduino.telegrambot.feature.duocards.sentence.model.AnswerCheckResult;
import com.arduino.telegrambot.feature.duocards.sentence.model.GeneratedExample;
import com.arduino.telegrambot.feature.physik.model.SectionProgress;
import com.arduino.telegrambot.model.DailyStrikeInfo;
import com.arduino.telegrambot.model.DailyTaskInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TemplateProcessor {
    String processGreetingsTemplate(String firstname, String lastname, List<DailyTaskInfo> dailyTaskInfoList, List<DailyStrikeInfo> strikeInfoList, int completedPhysicsTaskCount, int physicsTaskGoalValue, int completedDuoCardsSentenceTaskCount, int duoCardsSentenceTaskGoalValue, int actualStrikeValue, int bestStrikeValue);
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
    String processAnkiUserProfileTemplate(List<AnkiMenuDeckInfo> ankiMenuDeckInfoList);
    String processDuoCardsUserProfileTemplate();
    String processDecksMenuTemplate(Map<String, Integer> strikesTemplateData);
    String processFrontCardTemplate(AnkiCurrentCard currentCard, AnkiDeckStats ankiDeckStats);
    String processMarkedFrontCardTemplate(AnkiCurrentCard currentCard, AnkiDeckStats ankiDeckStats);
    String processFrontCardDuoCardsTemplate(AnkiCurrentCard currentCard, AnkiDeckStats ankiDeckStats);
    String processBackCardTemplate(AnkiCurrentCard currentCard,  AnkiDeckStats ankiDeckStats);
    String processBackCardDuoCardsTemplate(AnkiCurrentCard currentCard,  AnkiDeckStats ankiDeckStats);

    String processCompletedDeckTemplate(String deckname);

    String processAppsInfoTemplate();

    String processConfirmDeleteFrontCardTemplate(AnkiCurrentCard currentCard, AnkiDeckStats ankiDeckStats);
    String processConfirmDeleteBackCardTemplate(AnkiCurrentCard currentCard, AnkiDeckStats ankiDeckStats);

    String processAddNewNoteMessageTemplate();

    String processExampleNewCard(String newNote, String translation, String example);

    String processPreviewSavedDuoCard(String newNote, String translation, String example);

    String processExerciseSentanceTemplate(String translation);

    String processResultSentenceExercise();

    String processDuoCardResultSentenceExercise(AnswerCheckResult answerCheckResult, GeneratedExample generatedExample, String handler);

    String processSaveMediaMessage();

    String processAdminMenuTemplate();

    String processSuccessSaveMediaMessage(String fileId, String key);
}
