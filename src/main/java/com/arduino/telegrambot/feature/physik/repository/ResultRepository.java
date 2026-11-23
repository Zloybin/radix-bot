package com.arduino.telegrambot.feature.physik.repository;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.physik.entity.Result;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;

@Repository
public interface ResultRepository extends CrudRepository<Result, Long> {

    @Query(value = """
        SELECT COUNT(*)
        FROM results r
        JOIN phys_tasks pt ON r.task = pt.id
        WHERE r.user_id = :userId
          AND r.result = true
          AND pt.section = :section
        """, nativeQuery = true)
    int countPassedResultsByUserAndSection(
            @Param("userId") long userId,
            @Param("section") String section
    );

    @Query(value = """
        SELECT COUNT(*)
        FROM results r
        JOIN phys_tasks pt ON r.task = pt.id
        WHERE r.user_id = :userId
          AND r.result = false
          AND pt.section = :section
        """, nativeQuery = true)
    int countFailedResultsByUserAndSection(
            @Param("userId") long userId,
            @Param("section") String section
    );

    int countByUserAndDueDate(User user, LocalDate date);

//    int countCompletedPhysTaskToday(User user);
}
