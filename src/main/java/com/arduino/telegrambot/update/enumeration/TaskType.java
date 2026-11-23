package com.arduino.telegrambot.update.enumeration;

public enum TaskType {
    PULLUPS("Отжимания"),
    ANKI("Все колоды Anki"),
    DUO_CARDS_SENTENCE("Выполнено <b>%d</b> из <b>%d</b> предложений"),
    PHYSICS("Решено <b>%d</b> из <b>%d</b> задач"),
    MEDITATION("Медитация");

    private final String description;

    TaskType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}