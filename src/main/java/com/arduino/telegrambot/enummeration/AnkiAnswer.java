package com.arduino.telegrambot.enummeration;

public enum AnkiAnswer {

    AGAIN(1),
    HARD(2),
    GOOD(3),
    EASY(4);

    private final int index;

    AnkiAnswer(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
