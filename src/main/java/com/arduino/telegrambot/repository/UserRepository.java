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

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.state = :state WHERE u.id = :userId")
    int updateState(@Param("userId") Long userId,
                    @Param("state") UserState state);

    @Modifying
    @Transactional
    @Query(value = """
        ALTER TABLE users
        DROP CONSTRAINT users_state_check;

        ALTER TABLE users
        ADD CONSTRAINT users_state_check
        CHECK (
            state IN (
                'START',
                'WAIT_TASK',
                'WAIT_DECK_NAME'
            )
        )
        """, nativeQuery = true)
    void updateUserStateConstraint();
}
