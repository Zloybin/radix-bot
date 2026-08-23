package com.arduino.telegrambot.template;

public interface TemplateProcessor {
    String processGreetingsTemplate();
    String processUserProfileTemplate(String userName, long tasksCount, long completedTaskCount);
    String processRadTaskTemplate(String source, String target, String task);
    String processPhysTaskTemplate(String title, long taskNumber, int selfNumber, String taskLevel, String taskText, int pageNumber);
    String processPhysTaskWithAiTemplate(String title, long taskNumber, int selfNumber, String taskLevel, String taskText, int pageNumber, String aiAnswer, String userAnswer);
    String processPhysTaskWaitAiTemplate(String title, long taskNumber, int selfNumber, String taskLevel, String taskText, int pageNumber);
    String processSuccessCorrectTemplate(long taskId, boolean result);
    String processConfirmPhysTaskTemplate();
    String processUserResultMessageTemplate(boolean result, String rightAnswer, String userAnswer);

}
