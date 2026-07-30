package com.arduino.telegrambot.handle;

import com.arduino.telegrambot.model.UserRequest;

public class NewTaskHandler implements UpdateHandler{
    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "newTask".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {

    }
}
