package com.thales.taskmanager.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.thales.taskmanager.dto.TaskDTO;
import com.thales.taskmanager.enums.Priority;

public interface TaskRepository extends JpaRepository<TaskDTO, String> {

    Page<TaskDTO> findByCreatedByAndIsDeletedOrderByTitleDesc(String createdBy, boolean isDeleted, Pageable pageable);

    Page<TaskDTO> findByPriorityAndCreatedByAndIsDeletedOrderByTitleDesc(Priority priority, String createdBy,
            boolean isDeleted,
            Pageable pageable);

    Page<TaskDTO> findByDueDateAndCreatedByAndIsDeletedOrderByTitleDesc(LocalDate dueDate, String createdBy,
            boolean isDeleted,
            Pageable pageable);

    Page<TaskDTO> findByPriorityAndDueDateAndCreatedByAndIsDeletedOrderByTitleDesc(Priority priority, LocalDate dueDate,
            String createdBy, boolean isDeleted, Pageable pageable);
}
