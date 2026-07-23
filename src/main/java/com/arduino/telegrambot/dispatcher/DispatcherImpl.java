package com.arduino.telegrambot.dispatcher;

import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class DispatcherImpl implements Dispatcher {

    private final List<UpdateHandler> handlers;

    public DispatcherImpl(List<UpdateHandler> handlers) {
        this.handlers =
                handlers.stream().toList();
    }

    @Override
    public void dispatch(UserRequest userRequest) {

        for (UpdateHandler handler : handlers) {
            if (handler.isApplicable(userRequest.getRequest())){
                handler.handle(userRequest);
            }
        }

    }
}
