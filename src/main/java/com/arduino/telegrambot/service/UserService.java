package com.arduino.telegrambot.service;

import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.anki.service.AnkiServiceImpl;
import com.arduino.telegrambot.feature.anki.util.AnkiUtility;
import com.arduino.telegrambot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private AnkiServiceImpl ankiService;

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

    public User buildDefaultUser(Long chatId, String firstname, String lastname, String username) {
        var decks = ankiService.getDeckNames().block();
        var filteredDeckList = decks.stream().filter(deck -> !AnkiUtility.EXCLUDED_DECKS.contains(deck)).toList();




//        Map<String, AnkiDeckDailyInfo> defaultDeckStatMap = new HashMap<>();
//
//        AnkiDeckDailyInfo defaultAnkiDeckDailyInfo = AnkiDeckDailyInfo.builder()
//                .newCount(0)
//                .reviewCount(0)
//                .learnCount(0)
//                .totalInDeck(0)
//                .build();

//        decks.forEach(deckName -> defaultDeckStatMap.put(deckName, defaultAnkiDeckDailyInfo));

        var user = User.builder()
                .id(chatId)
                .firstname(firstname)
                .lastname(lastname)
                .username(username)
                .state(UserState.FREE)
                .physTaskId(0)
                .radTask("")
                .messageId(0)
                .isExcluded(false)
                .isAnswerSide(false)
//                .lastReviewId(0)
                .results(new ArrayList<>())
                .build();


//        TaskCompletionDailyInfo defaultTaskCompletionDailyInfo = TaskCompletionDailyInfo.builder()
//                .user(user)
//                .completedPhysTaskCount(0)
//                .completedDuoCardsSentenceTaskCount(0)
//                .ankiDeckDailyInfoMap(defaultDeckStatMap)
//                .build();
//
//
//        Map<LocalDate, TaskCompletionDailyInfo> dayStrikeInfoMap = new HashMap<>();
//        LocalDate now = LocalDate.now(ZoneId.systemDefault());
//        dayStrikeInfoMap.put(now, defaultTaskCompletionDailyInfo);

//        user.setDayTaskCompletionInfoMap(dayStrikeInfoMap);

        return user;
    }
}
