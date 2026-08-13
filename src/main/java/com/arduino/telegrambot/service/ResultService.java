package com.arduino.telegrambot.service;

import com.arduino.telegrambot.entity.Result;
import com.arduino.telegrambot.repository.ResultRepository;
import org.hibernate.query.internal.ResultMementoInstantiationStandard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    public Result save(Result result){
        return resultRepository.save(result);
    }

}
