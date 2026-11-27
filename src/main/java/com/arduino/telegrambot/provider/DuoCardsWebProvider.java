package com.arduino.telegrambot.provider;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.duocards.web.service.DuoCardsService;
import com.arduino.telegrambot.enummeration.TaskStatus;
import com.arduino.telegrambot.enummeration.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DuoCardsWebProvider implements TaskStatusProvider {

    @Autowired
    private DuoCardsService duoCardsService;

    @Override
    public TaskType getSource() {
        return TaskType.DUO_CARDS_WEB;
    }

    @Override
    public TaskStatus getStatus(User user, LocalDate date) {
        boolean streakReceivedToday = duoCardsService.isStreakReceivedToday();
        return streakReceivedToday ? TaskStatus.COMPLETED : TaskStatus.NOT_COMPLETED;
    }
}