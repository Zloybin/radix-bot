package com.arduino.telegrambot.service;

import com.arduino.telegrambot.entity.DeckProgress;
import com.arduino.telegrambot.enummeration.DeckStatus;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.anki.model.AnkiDeckStats;
import com.arduino.telegrambot.feature.anki.service.AnkiServiceImpl;
import com.arduino.telegrambot.feature.anki.util.AnkiUtility;
import com.arduino.telegrambot.repository.UserRepository;
import com.arduino.telegrambot.statistick.model.DayTaskCompletionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
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

        Map<String, AnkiDeckStats> defaultDeckStatMap = new HashMap<>();

        AnkiDeckStats defaultAnkiDeckStats = AnkiDeckStats.builder()
                .newCount(0)
                .reviewCount(0)
                .learnCount(0)
                .totalInDeck(0)
                .build();

        decks.forEach(deckName -> defaultDeckStatMap.put(deckName, defaultAnkiDeckStats));


        DayTaskCompletionInfo defaultDayTaskCompletionInfo = DayTaskCompletionInfo.builder()
                .user(user)
                .completedPhysTaskCount(0)
                .completedDuoCardsSentenceTaskCount(0)
                .ankiDeckStatsMap(defaultDeckStatMap)
                .build();


        Map<LocalDate, DayTaskCompletionInfo> dayStrikeInfoMap = new HashMap<>();
        LocalDate now = LocalDate.now(ZoneId.systemDefault());
        dayStrikeInfoMap.put(now, defaultDayTaskCompletionInfo);

        user.setDayStrikeInfoMap(dayStrikeInfoMap);

        return user;
    }
}
