package com.arduino.telegrambot.enummeration;

public enum AnswerType {
    SHORT ("Короткий"),
    DETAILED("Развернутый");

    private String title;

    AnswerType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
