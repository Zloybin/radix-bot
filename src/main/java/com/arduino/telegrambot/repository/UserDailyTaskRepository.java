package com.arduino.telegrambot.repository;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.entity.UserDailyTask;
import com.arduino.telegrambot.entity.UserDailyTaskId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserDailyTaskRepository extends CrudRepository<UserDailyTask, UserDailyTaskId> {
    List<UserDailyTask> findAllByUserAndUserDailyTaskId_Date(User user, LocalDate date);

    @Query(value = """
        SELECT date
        FROM user_daily_tasks
        WHERE user_id = :userId
          AND status = 'NOT_COMPLETED'
          AND required_for_strike_snapshot = true
        ORDER BY date DESC
        LIMIT 2
        """, nativeQuery = true)
    List<LocalDate> findFailedDateList(@Param("userId") Long userId);
}
