package com.thales.taskmanager.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.thales.taskmanager.dto.Task;
import com.thales.taskmanager.enums.Priority;

public interface TaskRepository extends JpaRepository<Task, String> {

    Page<Task> findAll(Pageable pageable);

    Page<Task> findByPriority(Priority priority, Pageable pageable);

    Page<Task> findByDueDate(LocalDate dueDate, Pageable pageable);

    Page<Task> findByPriorityAndDueDate(Priority priority, LocalDate dueDate, Pageable pageable);
}
