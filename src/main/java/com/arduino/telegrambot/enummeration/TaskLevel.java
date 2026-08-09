package com.arduino.telegrambot.enummeration;

public enum TaskLevel {
    INITIAL ("Начальный"),
    MIDDLE("Средний"),
    SUFFICIENT("Достаточный"),
    HIGH("Высокий");

    private final String title;

    TaskLevel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}