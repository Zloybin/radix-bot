package com.arduino.telegrambot.enummeration;

public enum NumberSystem {
    DEC ("десятичная"),
    HEX("шестнадцатиричная"),
    BIN("двоичная");

    private final String title;

    NumberSystem(String title) {

        this.title = title;

    }

    public String getTitle() {
        return title;
    }
}
