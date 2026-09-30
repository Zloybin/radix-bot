package com.arduino.telegrambot.entity;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@AllArgsConstructor @NoArgsConstructor
@Builder
@Setter @Getter
public class DuoCardExample {
    private String sentence;
    private String example;
    private String translation;
}
