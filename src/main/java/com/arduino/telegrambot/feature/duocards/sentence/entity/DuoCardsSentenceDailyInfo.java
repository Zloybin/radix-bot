package com.arduino.telegrambot.feature.duocards.sentence.entity;

import com.arduino.telegrambot.entity.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "duo_cards_sentence_daily_info")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DuoCardsSentenceDailyInfo {
    @EmbeddedId
    private DuoCardsSentenceDailyInfoId duoCardsSentenceDailyInfoId;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "completed_duocards_sentence_task_count")
    private int completedDuoCardsSentenceTaskCount;

    @Column(name = "duocard_sentence_goal")
    private int duoCardsSentenceGoal;
}
