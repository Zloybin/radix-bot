package com.arduino.telegrambot.repository;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.enummeration.UserState;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Modifying
    @Transactional
    @Query(value = """
        ALTER TABLE users
        DROP CONSTRAINT users_state_check;

        ALTER TABLE users
        ADD CONSTRAINT users_state_check
        CHECK (
            state IN (
                'TASK',
                'FREE',
                'WAIT_USER_RAD_ANSWER',
                'WAIT_ANKI_ANSWER',
                'WAIT_USER_PHYS_ANSWER',
                'WAIT_DECK_NAME'
            )
        )
        """, nativeQuery = true)
    void updateUserStateConstraint();
}
