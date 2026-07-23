package com.arduino.telegrambot.repository;

import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepository {
    private final List<User> users = new ArrayList<>();


    public UserRepository() {
        User user = User.builder()
                .state(UserState.FREE)
                .id(1)
                .build();

        users.add(user);
    }

    public User getUser() {
        return users.get(0);
    }
}
