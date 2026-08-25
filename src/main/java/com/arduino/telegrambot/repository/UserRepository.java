package com.arduino.telegrambot.repository;

import com.arduino.telegrambot.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET is_excluded = false WHERE id = 123", nativeQuery = true)
    void deleteFromUser();
}
