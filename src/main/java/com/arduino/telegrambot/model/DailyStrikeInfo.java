package com.arduino.telegrambot.model;

import com.arduino.telegrambot.update.enumeration.TaskStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class DailyStrikeInfo {
    private LocalDate date;
    private String day;
    private TaskStatus taskStatus;
}
