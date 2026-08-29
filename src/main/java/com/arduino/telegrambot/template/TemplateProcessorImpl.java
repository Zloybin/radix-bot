package com.arduino.telegrambot.template;

import com.arduino.telegrambot.anki.AnkiCurrentCard;
import com.arduino.telegrambot.model.SectionProgress;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Component
public class TemplateProcessorImpl implements TemplateProcessor{

    @Autowired
    private TemplateEngine engine;

    @Override
    public String processGreetingsTemplate(){
        return engine.process("greetings", new Context());
    }

    @Override
    public String processUserProfileTemplate(String userName, long tasksCount, long completedTaskCount) {
        var context = new Context();

        context.setVariable("userName", userName);
        context.setVariable("tasksCount", tasksCount);
        context.setVariable("completedTaskCount", completedTaskCount);

        return engine.process("user_profile", context);
    }

    @Override
    public String processRadTaskTemplate(String source, String target, String task) {
        Context context = new Context();
        context.setVariable("source", source);
        context.setVariable("drain", target);
        context.setVariable("task", task);

        return engine.process("rad_task_message", context);
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
        return engine.process("phys_task_message", context);
    }

    @Override
    public String processPhysTaskWithAiTemplate(String title, long taskNumber, int selfNumber, String taskLevel, String taskText, int pageNumber, String aiAnswer, String userAnswer) {
        var context = new Context();
        context.setVariable("title", title);
        context.setVariable("taskNumber", taskNumber);
        context.setVariable("selfTaskNumber", selfNumber);
        context.setVariable("taskLevel", taskLevel);
        context.setVariable("taskText", taskText);
        context.setVariable("pageNumber", pageNumber);
        context.setVariable("aiAnswer", aiAnswer);
        context.setVariable("userAnswer", userAnswer);
        return engine.process("phys_task_message_with_ai", context);
    }

    @Override
    public String processPhysTaskWaitAiTemplate(String title, long taskNumber, int selfNumber, String taskLevel, String taskText, int pageNumber) {
        var context = new Context();
        context.setVariable("title", title);
        context.setVariable("taskNumber", taskNumber);
        context.setVariable("selfTaskNumber", selfNumber);
        context.setVariable("taskLevel", taskLevel);
        context.setVariable("taskText", taskText);
        context.setVariable("pageNumber", pageNumber);
        return engine.process("phys_task_message_wait_ai", context);
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
    public String processUserResultMessageTemplate(boolean result, String rightAnswer, String userAnswer) {
        Context context = new Context();
        context.setVariable("result", result ? "Правильный ответ" : "Ответы не совпадают");
        context.setVariable("rightAnswer", rightAnswer);
        context.setVariable("userAnswer", userAnswer);

        return engine.process("user_result_message", context);
    }

    @Override
    public String processStatisticTemplate(List<SectionProgress> sectionProgresses) {
        Context context = new Context();
        context.setVariable("sections", sectionProgresses);
        return engine.process("statistic", context);
    }

    @Override
    public String processAnkiUserProfileTemplate() {
        Context context = new Context();
        return engine.process("anki_user_profile", context);
    }

    @Override
    public String processDecksMenuTemplate() {
        Context context = new Context();
        return engine.process("show_decks_template", context);
    }

    @Override
    public String processFrontCardTemplate(AnkiCurrentCard currentCard) {

        var question = Jsoup.parse(currentCard.question())
                .select(".question")
                .text();


        var tags = Jsoup.parse(currentCard.question())
                .select(".tags")
                .text();

        Context context = new Context();

        context.setVariable("Front", question);
        context.setVariable("DisplayTags", tags);
        return engine.process("anki_front", context);
    }

    @Override
    public String processBackCardTemplate(AnkiCurrentCard currentCard) {

        var question = Jsoup.parse(currentCard.question())
                .select(".question")
                .text();


        var tags = Jsoup.parse(currentCard.question())
                .select(".tags")
                .text();

        var answer = Jsoup.parse(currentCard.answer())
                .select(".answer")
                .text();

        Context context = new Context();

        context.setVariable("Front", question);
        context.setVariable("DisplayTags", tags);
        context.setVariable("Back", answer);
        return engine.process("anki_back", context);
    }

}
