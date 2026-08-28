package com.arduino.telegrambot.enummeration;

public enum AnkiAnswer {

    AGAIN(1),
    HARD(2),
    GOOD(3),
    EASY(4);

    private final int value;

    AnkiAnswer(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
