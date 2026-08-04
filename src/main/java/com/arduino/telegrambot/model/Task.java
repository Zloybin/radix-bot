package com.arduino.telegrambot.model;

import com.arduino.telegrambot.enummeration.AnswerType;
import com.arduino.telegrambot.enummeration.TaskLevel;

public class Task {
    private Long id;
    private String title;
    private int taskNumber;
    private int selfTaskNumber;
    private TaskLevel taskLevel;
    private char taskLetter;
    private boolean hasImage;
    private int pageNumber;
    private AnswerType answerType;
    private String answer;
    private String pageLink;
    private String taskText;
}
