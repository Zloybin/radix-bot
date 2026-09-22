package com.arduino.telegrambot.ui.template;

import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.feature.anki.model.AnkiDeckStats;
import com.arduino.telegrambot.feature.anki.parser.TelegramLatexParser;
import com.arduino.telegrambot.feature.physik.model.SectionProgress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Map;

@Component
public class TemplateProcessorImpl implements TemplateProcessor{

    @Autowired
    private TemplateEngine engine;

    @Override
    public String processGreetingsTemplate(){

        var context = new Context();
//        context.setVariable("duoCardsStreak", "");
//        context.setVariable("duoCardsRemaining", "");
//        context.setVariable("physicsLastDay", "");
//        context.setVariable("physicsLastDayTasks", "");
//        context.setVariable("ankiDecks", "");
//        context.setVariable("deckName", "");
//        context.setVariable("deckTotalInDeck", "");
//        context.setVariable("deckNewCount", "");
//        context.setVariable("deckLearnCount", "");
//        context.setVariable("deckReviewCount", "");
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
        return engine.process("success_correct", context);
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
    public String processAnkiUserProfileTemplate(int numCardsReviewedToday) {
        Context context = new Context();
        context.setVariable("numCardsReviewedToday", numCardsReviewedToday);
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

        var question = currentCard.question();


        var tags = currentCard.tags();

        Context context = new Context();

        context.setVariable("newCount", ankiDeckStats.newCount());
        context.setVariable("learnCount", ankiDeckStats.learnCount());
        context.setVariable("reviewCount", ankiDeckStats.reviewCount());


        var parsedQuestion = TelegramLatexParser.parse(question);
        context.setVariable("question", parsedQuestion);
        context.setVariable("tags", tags);
        return engine.process("./anki/anki_front", context);
    }

    @Override
    public String processFrontCardDuoCardsTemplate(AnkiCurrentCard currentCard, AnkiDeckStats ankiDeckStats) {
        var question = currentCard.question();


        var tags = currentCard.tags();

        Context context = new Context();

        context.setVariable("newCount", ankiDeckStats.newCount());
        context.setVariable("learnCount", ankiDeckStats.learnCount());
        context.setVariable("reviewCount", ankiDeckStats.reviewCount());

        context.setVariable("question", question);
        context.setVariable("tags", tags);
        return engine.process("duocard_front", context);
    }

    @Override
    public String processBackCardTemplate(AnkiCurrentCard currentCard,  AnkiDeckStats ankiDeckStats) {

        var question = currentCard.question();


        var tags = currentCard.tags();

        var answer = currentCard.answer();

        Context context = new Context();

        context.setVariable("newCount", ankiDeckStats.newCount());
        context.setVariable("learnCount", ankiDeckStats.learnCount());
        context.setVariable("reviewCount", ankiDeckStats.reviewCount());

        var parsedQuestion = TelegramLatexParser.parse(question);
        var parsedAnswer = TelegramLatexParser.parse(answer);

        context.setVariable("question", parsedQuestion);
        context.setVariable("tags", tags);
        context.setVariable("answer", parsedAnswer);
        return engine.process("anki_back", context);
    }

    @Override
    public String processBackCardDuoCardsTemplate(AnkiCurrentCard currentCard, AnkiDeckStats ankiDeckStats) {
        var question = currentCard.question();


        var tags = currentCard.tags();
        var example = currentCard.example();

        var answer = currentCard.answer() + "<br/><br/><cite>" + question +"</cite>";

        Context context = new Context();

        context.setVariable("newCount", ankiDeckStats.newCount());
        context.setVariable("learnCount", ankiDeckStats.learnCount());
        context.setVariable("reviewCount", ankiDeckStats.reviewCount());

        context.setVariable("question", question);
        context.setVariable("tags", tags);
        context.setVariable("answer", answer);
        context.setVariable("example", example);
        return engine.process("duocard_back", context);
    }

    @Override
    public String processCompletedDeckTemplate(String deckname) {

        Context context = new Context();
        context.setVariable("deckName", deckname);

        return engine.process("completed_deck_message", context);
    }

    @Override
    public String processAppsInfoTemplate() {
        Context context = new Context();
        return engine.process("apps_info", context);
    }

    @Override
    public String processConfirmDeleteCardTemplate(AnkiCurrentCard currentCard, AnkiDeckStats deckStats) {
        var question = currentCard.question();


        var tags = currentCard.tags();

        Context context = new Context();

        context.setVariable("newCount", deckStats.newCount());
        context.setVariable("learnCount", deckStats.learnCount());
        context.setVariable("reviewCount", deckStats.reviewCount());


        var parsedQuestion = TelegramLatexParser.parse(question);
        context.setVariable("question", parsedQuestion);
        context.setVariable("tags", tags);
        return engine.process("./anki/confirm_delete_anki_card", context);
    }

}
