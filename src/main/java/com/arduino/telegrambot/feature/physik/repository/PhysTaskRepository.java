package com.arduino.telegrambot.feature.physik.repository;

import com.arduino.telegrambot.feature.physik.entity.PhysicsTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhysTaskRepository extends CrudRepository<PhysicsTask, Long> {
}
