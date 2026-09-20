package com.arduino.telegrambot.handler;

import com.arduino.telegrambot.model.UserRequest;
import org.springframework.stereotype.Component;

@Component
public interface UpdateHandler {
    boolean isApplicable(UserRequest userRequest);
    void handle(UserRequest userRequest);
}
