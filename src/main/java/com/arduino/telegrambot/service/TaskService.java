package com.arduino.telegrambot.service;

import com.arduino.telegrambot.converter.RadConverter;
import com.arduino.telegrambot.entity.Task;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.enummeration.NumberSystem;
import com.arduino.telegrambot.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

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

    public Task findById(Long id){
        Optional<Task> optionalUsTask = taskRepository.findById(id);
        if(optionalUsTask.isPresent()){
            return optionalUsTask.get();
        }else {
            throw new IllegalArgumentException(String.format("Задания с id: %d не существует.", id));
        }
    }

    public long count() {
        return taskRepository.count();
    }

    public int getRandomPhysTaskId(){
        return new Random().nextInt((int) count());
    }

    public Long getRandomPhysTaskId(List<Long> completedTaskIds){
        List<Long> uncompletedTask = taskRepository.findIdsByIdIn(completedTaskIds);
        int randomId = random.nextInt(uncompletedTask.size() - 1);
        return uncompletedTask.get(randomId);
    }
}
