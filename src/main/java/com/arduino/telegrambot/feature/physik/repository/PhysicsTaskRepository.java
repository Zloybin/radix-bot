package com.arduino.telegrambot.feature.physik.repository;

import com.arduino.telegrambot.feature.physik.entity.PhysicsTask;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.physik.enummeration.Section;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PhysicsTaskRepository extends CrudRepository<PhysicsTask, Long> {
    @Query("SELECT pt.id FROM PhysicsTask pt WHERE pt.id NOT IN :ids")
    List<Long> findIdsByIdIn(@Param("ids") List<Long> ids);

    int countBySection(Section section);
}
