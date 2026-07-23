package com.arduino.telegrambot.service;

import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.model.User;
import com.arduino.telegrambot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserService {
    private static final Map<Long, User> userDataBase = new ConcurrentHashMap<>();

    @Autowired
    private UserRepository userRepository;


    // User

    public boolean isUserExist(Long chatId) {
        return userDataBase.get(chatId) != null;
    }

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


    // Default user

    public User buildAndPutDefaultUser(Long chatId) {
        var deafaultUser = buildDefaultUser(chatId);
        userDataBase.put(chatId, deafaultUser);
        return deafaultUser;
    }

    public User buildDefaultUser(Long chatId) {
        return User.builder()
                .id(chatId)
                .state(UserState.FREE)
                .build();
    }
}
