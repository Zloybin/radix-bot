package com.arduino.telegrambot.service;

import com.arduino.telegrambot.entity.Callback;
import com.arduino.telegrambot.repository.CallbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CallbackService {
    @Autowired
    private CallbackRepository callbackRepository;

    public Callback findById(Long id){
        Optional<Callback> optionalCallback = callbackRepository.findById(id);
        if(optionalCallback.isPresent()){
            return optionalCallback.get();
        }else {
            throw new IllegalArgumentException(String.format("Callback с id: %d не существует.", id));
        }
    }

    public boolean existBy(Long id){
        return callbackRepository.existsById(id);
    }

    public Callback save(Callback callback) {
        return callbackRepository.save(callback);
    }
}
