package com.arduino.telegrambot.model;

import com.arduino.telegrambot.enummeration.AnswerType;
import com.arduino.telegrambot.enummeration.TaskLevel;
import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "task_number", nullable = false)
    private int taskNumber;

    @Column(name = "self_task_number", nullable = false)
    private int selfTaskNumber;

    @Column(name = "task_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskLevel taskLevel;

    @Column(name = "task_letter", nullable = false)
    private char taskLetter;

    @Column(name = "has_image", nullable = false)
    private boolean hasImage;

    @Column(name = "page_number", nullable = false)
    private int pageNumber;

    @Column(name = "answer_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AnswerType answerType;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "page_link", nullable = false)
    private String pageLink;

    @Column(name = "task_text", nullable = false)
    private String taskText;
}
