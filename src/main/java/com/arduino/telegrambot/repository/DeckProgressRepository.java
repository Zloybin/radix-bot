package com.arduino.telegrambot.repository;

import com.arduino.telegrambot.entity.DeckProgress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeckProgressRepository extends CrudRepository<DeckProgress, Long>{

}
