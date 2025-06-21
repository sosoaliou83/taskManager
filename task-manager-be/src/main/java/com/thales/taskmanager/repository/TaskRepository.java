package com.thales.taskmanager.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.thales.taskmanager.dto.Task;
import com.thales.taskmanager.enums.Priority;

public interface TaskRepository extends JpaRepository<Task, String> {

    Page<Task> findAll(Pageable pageable);

    Page<Task> findByCreatedBy(String createdBy, Pageable pageable);

    Page<Task> findByPriorityAndCreatedBy(Priority priority, String createdBy, Pageable pageable);

    Page<Task> findByDueDateAndCreatedBy(LocalDate dueDate, String createdBy, Pageable pageable);

    Page<Task> findByPriorityAndDueDateAndCreatedBy(Priority priority, LocalDate dueDate, String createdBy,
            Pageable pageable);
}
