package com.arduino.telegrambot.repository;

import com.arduino.telegrambot.entity.Task;
import com.arduino.telegrambot.enummeration.Section;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    @Query("SELECT t.id FROM Task t WHERE t.id NOT IN :ids")
    List<Long> findIdsByIdIn(@Param("ids") List<Long> ids);

    int countBySection(Section section);
}
