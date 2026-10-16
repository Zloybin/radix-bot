package com.arduino.telegrambot.statistick.model;


import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.anki.model.AnkiDeckStats;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "day_task_completion_info")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class DayTaskCompletionInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "day")
    private LocalDate day;

    private int completedPhysTaskCount;
    private int completedDuoCardsSentenceTaskCount;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "day_deck_info",
            joinColumns = @JoinColumn(name = "day_task_completion_info_id"))
    @MapKeyColumn(name = "deck_name")
    private Map<String, AnkiDeckStats> ankiDeckStatsMap;
}
