package com.arduino.telegrambot.service;

import com.arduino.telegrambot.entity.DeckProgress;
import com.arduino.telegrambot.enummeration.DeckStatus;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.anki.service.AnkiService;
import com.arduino.telegrambot.feature.anki.util.AnkiUtility;
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
    private AnkiService ankiService;

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
        var decks = ankiService.getDecks().block();
        var filteredDeckList = decks.stream().filter(deck -> !AnkiUtility.EXCLUDED_DECKS.contains(deck)).toList();

        var user = User.builder()
                .id(chatId)
                .name(name)
                .messageId(0)
                .state(UserState.FREE)
                .task("")
                .isExcluded(false)
                .build();

        List<DeckProgress> deckProgressList = new ArrayList<>();
        for (String deck : filteredDeckList) {
            var deckProgress = DeckProgress.builder()
                    .deckStatus(DeckStatus.NOT_STARTED)
                    .localDate(0L)
                    .deckName(deck)
                    .build();

            deckProgressList.add(deckProgress);
        }

        user.setDeckProgress(deckProgressList);

        return user;
    }
}
