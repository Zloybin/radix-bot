package com.arduino.telegrambot.service;

import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<User> getAll() {

        List<User> list = new ArrayList<>();
        Iterable<User> iterator = userRepository.findAll();
        iterator.forEach(list::add);
        return list;
    }

//    public User findByIdOrDefault(Long chatId) {
//        Optional<User> optionalUser = userRepository.findById(chatId);
//        return optionalUser.orElseGet(() -> buildDefaultUser(chatId));
//    }

    // Default user

    public User buildDefaultUser(Long chatId, String name) {
        return User.builder()
                .id(chatId)
                .name(name)
                .messageId(0)
                .state(UserState.FREE)
                .task("")
                .isExcluded(false)
                .build();
    }
}
