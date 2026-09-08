package com.arduino.telegrambot.repository;

import com.arduino.telegrambot.entity.Callback;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallbackRepository extends CrudRepository<Callback, Long> {
}
