package com.arduino.telegrambot.enummeration;

public enum TaskLevel {
    BEGINNER ("Начальный"),
    INTERMEDIATE("Средний"),
    COMPETENT("Достаточный"),
    ADVANCED("Высокий");

    private final String title;

    TaskLevel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}