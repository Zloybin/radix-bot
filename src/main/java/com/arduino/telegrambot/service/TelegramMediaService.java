package com.arduino.telegrambot.service;

import com.arduino.telegrambot.entity.TelegramMedia;
import com.arduino.telegrambot.repository.TelegramMediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TelegramMediaService {

    @Autowired
    private TelegramMediaRepository telegramMediaRepository;

    public TelegramMedia findByKey(String key){
        return telegramMediaRepository.findById(key).orElseThrow(() -> new RuntimeException(String.format("Не найден media с ключом: %s", key)));
    }

    public void save(TelegramMedia media){
        telegramMediaRepository.save(media);
    }

}
