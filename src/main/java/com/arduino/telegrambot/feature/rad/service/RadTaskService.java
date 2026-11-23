package com.arduino.telegrambot.feature.rad.service;

import com.arduino.telegrambot.feature.rad.converter.RadConverter;
import com.arduino.telegrambot.feature.rad.enummeration.NumberSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class RadTaskService {

    public static final int BOUND = 129;
    private Random random = new Random();

    @Autowired
    private RadConverter radConverter;


    public String generateTask() {

        var values = new ArrayList<>(Arrays.asList(NumberSystem.values()));
        int sourceRandomIndex = random.nextInt(values.size());
        var sourceSys = values.get(sourceRandomIndex);

        int task = random.nextInt(BOUND);

        String taskValue;

        switch (sourceSys) {

            case NumberSystem.HEX -> taskValue = radConverter.convertDecimalToHex(task);
            case NumberSystem.BIN -> taskValue = radConverter.convertDecimalToBinary(task);
            case NumberSystem.DEC -> taskValue = String.valueOf(task);
            default -> throw new RuntimeException(String.format("Недопустимое значение: %s",  sourceSys) );

        }

        values.remove(sourceSys);

        int drainRandomSys = random.nextInt(values.size() - 1);
        var drainSys = values.get(drainRandomSys);

        return sourceSys.name() + drainSys.name() + taskValue;
    }
}
