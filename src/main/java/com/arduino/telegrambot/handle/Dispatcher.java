package com.arduino.telegrambot.handle;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public interface Dispatcher {
    void dispatch(Update update);
}
