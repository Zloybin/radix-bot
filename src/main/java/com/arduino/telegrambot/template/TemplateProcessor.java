package com.arduino.telegrambot.template;

import org.thymeleaf.context.Context;

public interface TemplateProcessor {
    String processGreetingsTemplate();
    String processRadTaskTemplate(String source, String target, String task);
    String processPhysTaskTemplate(String title, long taskNumber, int selfNumber, String taskLevel, String taskText, int pageNumber);
    String processSuccessCorrectTemplate(long taskId, boolean result);
    String processConfirmPhysTaskTemplate();
    String processUserResultMessageTemplate(boolean result, String rightAnswer, String userAnswer);

}
