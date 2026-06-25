package com.arduino.telegrambot.service;

import com.arduino.telegrambot.keyboard.MenuType;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserStateService {

    private final Map<Long, MenuType> userStates = new ConcurrentHashMap<>();

    public MenuType getUserState(Long chatId) {
        return userStates.getOrDefault(chatId, MenuType.MAIN);
    }

    public void setUserState(Long chatId, MenuType menuType) {
        userStates.put(chatId, menuType);
    }

    public void resetUserState(Long chatId) {
        userStates.remove(chatId);
    }
}