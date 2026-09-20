package com.arduino.telegrambot.enummeration;

public enum NumberSystem {
    DEC ("десятичная", "десятиричной", "десятиричную"),
    HEX("шестнадцатиричная", "шестнадцатиричной", "шестнадцатиричную"),
    BIN("двоичная", "двоичной", "двоичную");

    private final String title;
    private final String dativ;
    private final String akusativ;

    NumberSystem(String title, String dativ, String akusativ) {

        this.title = title;
        this.dativ = dativ;
        this.akusativ = akusativ;

    }

    public String getTitle() {
        return title;
    }

    public String getDativ() {
        return dativ;
    }

    public String getAkusativ() {
        return akusativ;
    }
}
