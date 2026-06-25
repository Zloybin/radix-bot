package com.arduino.telegrambot.service;

import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.model.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserDBService {

    private static final Map<Long, User> userDataBase = new ConcurrentHashMap<>();

    // User

    public void putUser(Long chatId, User user) {
        userDataBase.put(chatId, user);
    }

    public User getUser(Long chatId) {
        return userDataBase.get(chatId);
    }

    public User getOrDefault(Long chatId) {
        var user = getUser(chatId);
        return user == null ? buildAndPutDefaultUser(chatId) : user;
    }


    // User state

    public void setUserState(Long chatId, UserState userState) {
        var user = getUser(chatId);
        user.setState(userState);
    }

    public UserState getUserState(Long chatId) {
        if (userDataBase.get(chatId) == null) {
            throw new IllegalArgumentException(String.format("Нет пользователя с таким id: %s", chatId));
        }
        return userDataBase.get(chatId).getState();
    }


    // Default user

    private static User buildAndPutDefaultUser(Long chatId) {
        var deafaultUser = buildDefaultUser(chatId);
        userDataBase.put(chatId, deafaultUser);
        return deafaultUser;
    }

    private static User buildDefaultUser(Long chatId) {
        return User.builder()
                .id(chatId)
                .state(UserState.FREE)
                .build();
    }
}