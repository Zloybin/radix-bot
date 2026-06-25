package com.arduino.telegrambot.service;

import com.arduino.telegrambot.enummeration.NumberSystem;
import org.jvnet.hk2.annotations.Service;

import java.util.List;
import java.util.Random;

@Service
public class TaskService {
    private Random random = new Random();

    public String generateTask() {

        var values = List.of(NumberSystem.values());
        int sourceRandomIndex = random.nextInt(values.size() - 1);
        var sourceSys = values.get(sourceRandomIndex);

        values.remove(sourceRandomIndex);

        int drainRandomSys = random.nextInt(values.size() - 1);
        var drainSys = values.get(drainRandomSys);

        int task = random.nextInt(129);
        return sourceSys.name() + drainSys.name() + task;
    }


}
