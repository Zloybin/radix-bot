package com.arduino.telegrambot.handle;

import com.arduino.telegrambot.model.UserRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public interface UpdateHandler {
    boolean isApplicable(UserRequest userRequest);
    void handle(UserRequest userRequest);
}
