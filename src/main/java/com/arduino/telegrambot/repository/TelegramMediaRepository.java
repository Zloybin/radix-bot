package com.arduino.telegrambot.repository;

import com.arduino.telegrambot.entity.TelegramMedia;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramMediaRepository extends CrudRepository<TelegramMedia, String> {
}
