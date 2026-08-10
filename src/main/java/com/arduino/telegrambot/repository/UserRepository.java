package com.arduino.telegrambot.repository;

import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
