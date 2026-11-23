package com.arduino.telegrambot.ui.template;

import com.arduino.telegrambot.feature.anki.model.AnkiMenuDeckInfo;
import com.arduino.telegrambot.feature.duocards.deck.enummeration.AnkiTemplate;
import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.feature.anki.model.raw.AnkiDeckStats;
import com.arduino.telegrambot.feature.anki.parser.TelegramLatexParser;
import com.arduino.telegrambot.feature.anki.util.AnkiUtility;
import com.arduino.telegrambot.feature.duocards.sentence.model.AnswerCheckResult;
import com.arduino.telegrambot.feature.duocards.sentence.model.GeneratedExample;
import com.arduino.telegrambot.feature.physik.model.SectionProgress;
import com.arduino.telegrambot.model.DailyStrikeInfo;
import com.arduino.telegrambot.model.DailyTaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TemplateProcessorImpl implements TemplateProcessor {

    @Autowired
    private TemplateEngine engine;

    @Override
    public String processGreetingsTemplate(String firstname, String lastname, List<DailyTaskInfo> dailyTaskInfoList, List<DailyStrikeInfo> strikeInfoList, int completedPhysicsTaskCount, int physicsTaskGoalValue, int completedDuoCardsSentenceTaskCount, int duoCardsSentenceTaskGoalValue, int actualStrikeValue, int bestStrikeValue) {

        for (DailyTaskInfo dailyTaskInfo : dailyTaskInfoList) {
            switch (dailyTaskInfo.getTaskType()){
                case PHYSICS -> dailyTaskInfo.setTaskDescription(String.format(dailyTaskInfo.getTaskDescription(), completedPhysicsTaskCount, physicsTaskGoalValue));
                case DUO_CARDS_SENTENCE -> dailyTaskInfo.setTaskDescription(String.format(dailyTaskInfo.getTaskDescription(), completedDuoCardsSentenceTaskCount, duoCardsSentenceTaskGoalValue));
            }
        }

        var context = new Context();

        //user data
        context.setVariable("firstname", firstname);
        context.setVariable("lastname", lastname);

        //task check-box
        context.setVariable("dailyTaskInfoList", dailyTaskInfoList);

        //strike info
        context.setVariable("actualStrikeValue", actualStrikeValue);
        context.setVariable("bestStrikeValue", bestStrikeValue);

        //strike table
        context.setVariable("strikeInfoList", strikeInfoList);

        return engine.process("greetings", context);
    }


    @Override
    public String processUserProfileTemplate(String userName, long tasksCount, long completedTaskCount) {
        var context = new Context();

        context.setVariable("userName", userName);
        context.setVariable("tasksCount", tasksCount);
        context.setVariable("completedTaskCount", completedTaskCount);

        return engine.process("./phys/user_profile", context);
    }

    @Override
    public String processRadTaskTemplate(String source, String target, String task) {
        Context context = new Context();
        context.setVariable("source", source);
        context.setVariable("drain", target);
        context.setVariable("task", task);

        return engine.process("./rad/rad_task_message", context);
    }

    @Override
    public String processPhysTaskTemplate(String section, String title, long taskNumber, int selfNumber, String taskLevel, String taskText, int pageNumber) {
        var context = new Context();
        context.setVariable("section", section);
        context.setVariable("title", title);
        context.setVariable("taskNumber", taskNumber);
        context.setVariable("selfTaskNumber", selfNumber);
        context.setVariable("taskLevel", taskLevel);
        context.setVariable("taskText", taskText);
        context.setVariable("pageNumber", pageNumber);
        return engine.process("./phys/phys_task_message", context);
    }

    @Override
    public String processPhysTaskWaitUserAnswerTemplate() {
        var context = new Context();
        return engine.process("./phys/phys_task_message_give_answer", context);
    }

    @Override
    public String processPhysTaskWithAiTemplate(String title, long taskNumber, int selfNumber, String taskLevel, String taskText, int pageNumber, String section, String aiAnswer, String userAnswer, String rightAnswer) {
        var context = new Context();
        context.setVariable("title", title);
        context.setVariable("taskNumber", taskNumber);
        context.setVariable("selfTaskNumber", selfNumber);
        context.setVariable("taskLevel", taskLevel);
        context.setVariable("taskText", taskText);
        context.setVariable("pageNumber", pageNumber);
        context.setVariable("aiAnswer", aiAnswer);
        context.setVariable("userAnswer", userAnswer);
        context.setVariable("rightAnswer", rightAnswer);
        context.setVariable("section", section);
        return engine.process("./phys/phys_task_message_with_ai", context);
    }

    @Override
    public String processPhysTaskWaitAiTemplate(String title, long taskNumber, int selfNumber, String taskLevel, String taskText, int pageNumber, String section, String userAnswer, String rightAnswer) {
        var context = new Context();
        context.setVariable("rightAnswer", rightAnswer);
        context.setVariable("userAnswer", userAnswer);
        context.setVariable("section", section);
        context.setVariable("title", title);
        context.setVariable("taskNumber", taskNumber);
        context.setVariable("selfTaskNumber", selfNumber);
        context.setVariable("taskLevel", taskLevel);
        context.setVariable("taskText", taskText);
        context.setVariable("pageNumber", pageNumber);
        return engine.process("./phys/phys_task_message_wait_ai", context);
    }

    @Override
    public String processSuccessCorrectTemplate(long taskId, boolean result) {
        var context = new Context();
        context.setVariable("id", taskId);
        context.setVariable("result", result);
        return engine.process("phys/success_correct", context);
    }

    @Override
    public String processConfirmPhysTaskTemplate() {
        var context = new Context();
        return engine.process("confirm_phys_task", context);
    }

    @Override
    public String processPhysUserResultMessageTemplate(boolean result, String rightAnswer, String userAnswer, String section, String title, long taskNumber, int selfNumber, String taskLevel, String taskText, int pageNumber) {
        Context context = new Context();
        context.setVariable("result", result ? "Правильный ответ" : "Ответы не совпадают");
        context.setVariable("rightAnswer", rightAnswer);
        context.setVariable("userAnswer", userAnswer);
        context.setVariable("section", section);
        context.setVariable("title", title);
        context.setVariable("taskNumber", taskNumber);
        context.setVariable("selfTaskNumber", selfNumber);
        context.setVariable("taskLevel", taskLevel);
        context.setVariable("taskText", taskText);
        context.setVariable("pageNumber", pageNumber);

        return engine.process("./phys/user_result_message", context);
    }

    @Override
    public String processRadUserResultMessageTemplate(boolean result, String rightAnswer, String userAnswer, String source, String target, String task) {
        Context context = new Context();
        context.setVariable("result", result ? "Правильный ответ" : "Ответы не совпадают");
        context.setVariable("rightAnswer", rightAnswer);
        context.setVariable("userAnswer", userAnswer);
        context.setVariable("source", source);
        context.setVariable("drain", target);
        context.setVariable("task", task);


        return engine.process("./rad/rad_user_result_message", context);
    }

    @Override
    public String processStatisticTemplate(List<SectionProgress> sectionProgresses) {
        Context context = new Context();
        context.setVariable("sections", sectionProgresses);
        return engine.process("./phys/statistic", context);
    }

    @Override
    public String processAnkiUserProfileTemplate(List<AnkiMenuDeckInfo> ankiMenuDeckInfoList) {
        Context context = new Context();
        context.setVariable("ankiMenuDeckInfoList", ankiMenuDeckInfoList);
        return engine.process("./anki/anki_user_profile", context);
    }

    @Override
    public String processDuoCardsUserProfileTemplate() {
        Context context = new Context();
        return engine.process("./duocards/duocards_user_profile", context);
    }

    @Override
    public String processDecksMenuTemplate(Map<String, Integer> strikesTemplateData) {
        Context context = new Context();
        context.setVariable("strikesTemplateData", strikesTemplateData);
        return engine.process("./anki/show_decks_template", context);
    }

    @Override
    public String processFrontCardTemplate(AnkiCurrentCard currentCard, AnkiDeckStats ankiDeckStats) {

        var question = currentCard.getQuestion();
        var tags = currentCard.getTags();
        var flagValue = currentCard.getFlag();
        var flag = AnkiUtility.getFlagView(flagValue);

        Context context = new Context();

        context.setVariable("newCount", ankiDeckStats.getNewCount());
        context.setVariable("learnCount", ankiDeckStats.getLearnCount());
        context.setVariable("reviewCount", ankiDeckStats.getReviewCount());


        var parsedQuestion = TelegramLatexParser.parse(question);
        context.setVariable("question", parsedQuestion);
        context.setVariable("tags", tags);
        context.setVariable("flag", flag);
        return engine.process("./anki/anki_front", context);
    }

    @Override
    public String processMarkedFrontCardTemplate(AnkiCurrentCard currentCard, AnkiDeckStats ankiDeckStats) {

        var question = currentCard.getQuestion();


        var tags = currentCard.getTags();

        Context context = new Context();

        context.setVariable("newCount", ankiDeckStats.getNewCount());
        context.setVariable("learnCount", ankiDeckStats.getLearnCount());
        context.setVariable("reviewCount", ankiDeckStats.getReviewCount());


        var parsedQuestion = TelegramLatexParser.parse(question);
        context.setVariable("question", parsedQuestion);
        context.setVariable("tags", tags);
        return engine.process("./anki/marked_anki_front", context);
    }

    @Override
    public String processFrontCardDuoCardsTemplate(AnkiCurrentCard currentCard, AnkiDeckStats ankiDeckStats) {
        var question = AnkiTemplate.TEMPLATE_1.getTitile().equals(currentCard.getAnkiTemplate().getTitile())
                ? currentCard.getQuestion()
                : currentCard.getAnswer();

        var tags = currentCard.getTags();

        Context context = new Context();

        context.setVariable("newCount", ankiDeckStats.getNewCount());
        context.setVariable("learnCount", ankiDeckStats.getLearnCount());
        context.setVariable("reviewCount", ankiDeckStats.getReviewCount());

        context.setVariable("question", question);
        context.setVariable("tags", tags);
        return engine.process("duocards/duocard_front", context);
    }

    @Override
    public String processBackCardTemplate(AnkiCurrentCard currentCard, AnkiDeckStats ankiDeckStats) {

        var question = currentCard.getQuestion();

        var flag = AnkiUtility.getFlagView(currentCard.getFlag());


        var tags = currentCard.getTags();

        var answer = currentCard.getAnswer();

        Context context = new Context();

        context.setVariable("newCount", ankiDeckStats.getNewCount());
        context.setVariable("learnCount", ankiDeckStats.getLearnCount());
        context.setVariable("reviewCount", ankiDeckStats.getReviewCount());

        var parsedQuestion = TelegramLatexParser.parse(question);
        var parsedAnswer = TelegramLatexParser.parse(answer);

        context.setVariable("question", parsedQuestion);
        context.setVariable("tags", tags);
        context.setVariable("answer", parsedAnswer);
        context.setVariable("flag", flag);
        return engine.process("anki/anki_back", context);
    }

    @Override
    public String processBackCardDuoCardsTemplate(AnkiCurrentCard currentCard, AnkiDeckStats ankiDeckStats) {

        var question = AnkiTemplate.TEMPLATE_1.getTitile().equals(currentCard.getAnkiTemplate().getTitile())
                ? currentCard.getQuestion()
                : currentCard.getAnswer();

        var answer = AnkiTemplate.TEMPLATE_1.getTitile().equals(currentCard.getAnkiTemplate().getTitile())
                ? currentCard.getAnswer()
                : currentCard.getQuestion();


        var tags = currentCard.getTags();
        var example = currentCard.getExample();

        answer = answer + "<br/><br/><cite>" + question + "</cite>";

        Context context = new Context();

        context.setVariable("newCount", ankiDeckStats.getNewCount());
        context.setVariable("learnCount", ankiDeckStats.getLearnCount());
        context.setVariable("reviewCount", ankiDeckStats.getReviewCount());

        context.setVariable("question", question);
        context.setVariable("tags", tags);
        context.setVariable("answer", answer);
        context.setVariable("example", example);
        return engine.process("duocards/duocard_back", context);
    }

    @Override
    public String processCompletedDeckTemplate(String deckname) {

        Context context = new Context();
        context.setVariable("deckName", deckname);

        return engine.process("anki/completed_deck_message", context);
    }

    @Override
    public String processAppsInfoTemplate() {
        Context context = new Context();
        return engine.process("anki/apps_info", context);
    }

    @Override
    public String processConfirmDeleteFrontCardTemplate(AnkiCurrentCard currentCard, AnkiDeckStats ankiDeckStats) {
        var question = currentCard.getQuestion();
        var tags = currentCard.getTags();
        var flagValue = currentCard.getFlag();
        var flag = AnkiUtility.getFlagView(flagValue);

        Context context = new Context();

        context.setVariable("newCount", ankiDeckStats.getNewCount());
        context.setVariable("learnCount", ankiDeckStats.getLearnCount());
        context.setVariable("reviewCount", ankiDeckStats.getReviewCount());

        var parsedQuestion = TelegramLatexParser.parse(question);
        context.setVariable("question", parsedQuestion);
        context.setVariable("tags", tags);
        context.setVariable("flag", flag);
        return engine.process("./anki/confirm_delete_front_anki_card", context);
    }

    @Override
    public String processConfirmDeleteBackCardTemplate(AnkiCurrentCard currentCard, AnkiDeckStats ankiDeckStats) {
        var question = currentCard.getQuestion();

        var flag = AnkiUtility.getFlagView(currentCard.getFlag());


        var tags = currentCard.getTags();

        var answer = currentCard.getAnswer();

        Context context = new Context();

        context.setVariable("newCount", ankiDeckStats.getNewCount());
        context.setVariable("learnCount", ankiDeckStats.getLearnCount());
        context.setVariable("reviewCount", ankiDeckStats.getReviewCount());

        var parsedQuestion = TelegramLatexParser.parse(question);
        var parsedAnswer = TelegramLatexParser.parse(answer);

        context.setVariable("question", parsedQuestion);
        context.setVariable("tags", tags);
        context.setVariable("answer", parsedAnswer);
        context.setVariable("flag", flag);
        return engine.process("./anki/confirm_delete_back_anki_card", context);
    }

    @Override
    public String processAddNewNoteMessageTemplate() {
        Context context = new Context();
        return engine.process("./anki/add_new_note_message", context);
    }

    @Override
    public String processExampleNewCard(String newNote, String translation, String example) {

        Context context = new Context();
        context.setVariable("sentence", newNote);
        context.setVariable("translation", translation);
        context.setVariable("example", example);
        return engine.process("./anki/accept_new_note_template", context);
    }

    @Override
    public String processPreviewSavedDuoCard(String newNote, String translation, String example) {
        Context context = new Context();

        translation = translation + "<br/><br/><cite>" + newNote + "</cite>";
        context.setVariable("sentence", newNote);
        context.setVariable("translation", translation);
        context.setVariable("example", example);
        return engine.process("./duocards/duocard_new_preview", context);
    }

    @Override
    public String processExerciseSentanceTemplate(String translation) {
        Context context = new Context();

        context.setVariable("translation", translation);
        return engine.process("./duocards/exercise_sentence", context);
    }

    @Override
    public String processResultSentenceExercise() {
        return "";
    }

    @Override
    public String processDuoCardResultSentenceExercise(AnswerCheckResult answerCheckResult, GeneratedExample generatedExample, String userRequest) {

        Context context = new Context();

        context.setVariable("userRequest", userRequest);
        context.setVariable("answerCheckResult", answerCheckResult);
        context.setVariable("generatedExample", generatedExample);

        return engine.process("./duocards/result_sentences_exercise", context);
    }

    @Override
    public String processSaveMediaMessage() {
        Context context = new Context();
        return engine.process("save_media", context);
    }

    @Override
    public String processAdminMenuTemplate() {
        Context context = new Context();
        return engine.process("admin_menu", context);
    }

    @Override
    public String processSuccessSaveMediaMessage(String fileId, String key) {
        Context context = new Context();
        context.setVariable("key", key);
        context.setVariable("fileId", fileId);
        return engine.process("success_save_media", context);
    }

}
