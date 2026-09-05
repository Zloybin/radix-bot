package com.arduino.telegrambot.repository;

import com.arduino.telegrambot.entity.Result;
import com.arduino.telegrambot.enummeration.Section;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends CrudRepository<Result, Long> {

    @Query(value = """
        SELECT COUNT(*)
        FROM results r
        JOIN tasks t ON r.task = t.id
        WHERE r.user_id = :userId
          AND r.result = true
          AND t.section = :section
        """, nativeQuery = true)
    int countPassedResultsByUserAndSection(
            @Param("userId") long userId,
            @Param("section") String section
    );

    @Query(value = """
        SELECT COUNT(*)
        FROM results r
        JOIN tasks t ON r.task = t.id
        WHERE r.user_id = :userId
          AND r.result = false
          AND t.section = :section
        """, nativeQuery = true)
    int countFailedResultsByUserAndSection(
            @Param("userId") long userId,
            @Param("section") String section
    );
}
