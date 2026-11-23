package com.arduino.telegrambot.repository;

import com.arduino.telegrambot.feature.duocards.deck.entity.TelegramMedia;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramMediaRepository extends CrudRepository<TelegramMedia, String> {
}
