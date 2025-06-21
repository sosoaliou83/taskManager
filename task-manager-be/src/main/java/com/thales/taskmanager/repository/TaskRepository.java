package com.thales.taskmanager.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.thales.taskmanager.dto.TaskDTO;
import com.thales.taskmanager.enums.Priority;

public interface TaskRepository extends JpaRepository<TaskDTO, String> {

    Page<TaskDTO> findAll(Pageable pageable);

    Page<TaskDTO> findByCreatedBy(String createdBy, Pageable pageable);

    Page<TaskDTO> findByPriorityAndCreatedBy(Priority priority, String createdBy, Pageable pageable);

    Page<TaskDTO> findByDueDateAndCreatedBy(LocalDate dueDate, String createdBy, Pageable pageable);

    Page<TaskDTO> findByPriorityAndDueDateAndCreatedBy(Priority priority, LocalDate dueDate, String createdBy,
            Pageable pageable);
}
