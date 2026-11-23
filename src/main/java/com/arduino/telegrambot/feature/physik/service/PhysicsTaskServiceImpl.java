package com.arduino.telegrambot.feature.physik.service;

import com.arduino.telegrambot.feature.physik.entity.PhysicsTask;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.physik.enummeration.Section;
import com.arduino.telegrambot.feature.physik.repository.PhysicsTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PhysicsTaskServiceImpl implements PhysicsTaskService{

    private Random random = new Random();

    @Autowired
    private PhysicsTaskRepository physicsTaskRepository;

    public PhysicsTask findById(Long id){
        Optional<PhysicsTask> optionalUsTask = physicsTaskRepository.findById(id);
        if(optionalUsTask.isPresent()){
            return optionalUsTask.get();
        }else {
            throw new IllegalArgumentException(String.format("Задания с id: %d не существует.", id));
        }
    }

    public long count() {
        return physicsTaskRepository.count();
    }

    public int getRandomPhysTaskId(){
        return new Random().nextInt((int) count());
    }

    public Long getRandomPhysTaskId(List<Long> completedTaskIds){
        List<Long> uncompletedTask = physicsTaskRepository.findIdsByIdIn(completedTaskIds);
        int randomId = random.nextInt(uncompletedTask.size() - 1);
        return uncompletedTask.get(randomId);
    }

    public int getSectionTaskCount(Section section) {
        return physicsTaskRepository.countBySection(section);
    }

}
