package com.arduino.telegrambot.enummeration;

public enum AnkiAnswer {

    AGAIN(1, "Снова"),
    HARD(2, "Тяжело"),
    GOOD(3, "Хорошо"),
    EASY(4, "Легко");

    private final int index;
    private final String buttonText;

    AnkiAnswer(int index, String buttonText) {
        this.index = index;
        this.buttonText = buttonText;
    }

    public int getIndex() {
        return index;
    }

    public String getButtonText() {
        return buttonText;
    }
}
