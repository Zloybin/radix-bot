package com.arduino.telegrambot;

import com.arduino.telegrambot.converter.RadConverter;
import com.arduino.telegrambot.validator.AnswerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "org.telegram.telegrambots.meta.*"
))
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}