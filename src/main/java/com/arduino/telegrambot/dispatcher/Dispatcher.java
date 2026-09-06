package com.arduino.telegrambot.dispatcher;

import com.arduino.telegrambot.model.UserRequest;
import org.springframework.stereotype.Component;

@Component
public interface Dispatcher {
    void dispatch(UserRequest userRequest);
}
