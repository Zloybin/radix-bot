package com.arduino.telegrambot.service;

import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.model.User;
import com.arduino.telegrambot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // User

    public boolean existById(Long chatId) {
        return userRepository.existsById(chatId);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findById(Long chatId) {
        Optional<User> optionalUser = userRepository.findById(chatId);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }else {
            throw new IllegalArgumentException(String.format("Пользователя с id: %d не существует.", chatId));
        }
    }

//    public User findByIdOrDefault(Long chatId) {
//        Optional<User> optionalUser = userRepository.findById(chatId);
//        return optionalUser.orElseGet(() -> buildDefaultUser(chatId));
//    }

    // Default user

    public User buildDefaultUser(Long chatId) {
        return User.builder()
                .id(chatId)
                .state(UserState.FREE)
                .build();
    }
}
