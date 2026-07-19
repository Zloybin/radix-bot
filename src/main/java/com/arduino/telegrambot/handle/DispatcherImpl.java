package com.arduino.telegrambot.handle;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class DispatcherImpl implements Dispatcher {

    @Autowired
    UpdateHandler updateHandler;

    @Override
    public void dispatch(Update update) {
        String callback = update.getMessage().getText();
        if (updateHandler.isApplicable(callback)){
            updateHandler.handle(update);
        }
    }
}
