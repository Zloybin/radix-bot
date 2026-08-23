package com.arduino.telegrambot.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

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
    public String processPhysTaskTemplate(String title, long taskNumber, int selfNumber, String taskLevel, String taskText, int pageNumber) {
        var context = new Context();
        context.setVariable("title", title);
        context.setVariable("taskNumber", taskNumber);
        context.setVariable("selfTaskNumber", selfNumber);
        context.setVariable("taskLevel", taskLevel);
        context.setVariable("taskText", taskText);
        context.setVariable("pageNumber", pageNumber);
        return engine.process("phys_task_message", context);
    }

    @Override
    public String processPhysTaskWithAiTemplate(String title, long taskNumber, int selfNumber, String taskLevel, String taskText, int pageNumber, String aiAnswer) {
        var context = new Context();
        context.setVariable("title", title);
        context.setVariable("taskNumber", taskNumber);
        context.setVariable("selfTaskNumber", selfNumber);
        context.setVariable("taskLevel", taskLevel);
        context.setVariable("taskText", taskText);
        context.setVariable("pageNumber", pageNumber);
        context.setVariable("aiAnswer", aiAnswer);
        return engine.process("phys_task_message_with_ai", context);
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
        context.setVariable("result", result);
        context.setVariable("rightAnswer", rightAnswer);
        context.setVariable("userAnswer", userAnswer);

        return engine.process("user_result_message", context);
    }

}
