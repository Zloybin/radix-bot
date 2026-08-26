package com.arduino.telegrambot.enummeration;

public enum Section {
    INTRODUCTION("Введение"),
    MATTER_STRUCTURE("Начальные сведения о строении вещества"),
    MOTION("Механика движения"),
    FORCES("Силы в механике"),
    PRESSURE("Давление твердых тел, газов и жидкостей"),
    BUOYANCY("Архимедова сила и плавание тел"),
    ENERGY("Механическая работа, мощность и энергия");

    private final String russianName;

    Section(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }
}