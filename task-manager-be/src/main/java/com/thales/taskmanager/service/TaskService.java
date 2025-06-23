package com.thales.taskmanager.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thales.taskmanager.dto.TaskDTO;
import com.thales.taskmanager.dto.TaskRequest;
import com.thales.taskmanager.repository.TaskRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskDTO createTask(TaskDTO task) {
        log.info("Creating new task: {}", task.getTitle());
        task.setCreatedDate(LocalDate.now());
        TaskDTO saved = taskRepository.save(task);
        log.info("Task created with ID: {}", saved.getId());
        return saved;
    }

    public Page<TaskDTO> getTasks(TaskRequest request) {
        log.info("Fetching tasks for user: {} with filters [priority: {}, dueDate: {}, deleted: {}]",
                request.getCreatedBy(), request.getPriority(), request.getDueDate(), request.isDeleted());

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<TaskDTO> page;

        if (request.getPriority() != null && request.getDueDate() != null) {
            page = taskRepository.findByPriorityAndDueDateAndCreatedByAndIsDeletedOrderByTitleDesc(
                    request.getPriority(), request.getDueDate(), request.getCreatedBy(), request.isDeleted(), pageable);
        } else if (request.getPriority() != null) {
            page = taskRepository.findByPriorityAndCreatedByAndIsDeletedOrderByTitleDesc(
                    request.getPriority(), request.getCreatedBy(), request.isDeleted(), pageable);
        } else if (request.getDueDate() != null) {
            page = taskRepository.findByDueDateAndCreatedByAndIsDeletedOrderByTitleDesc(
                    request.getDueDate(), request.getCreatedBy(), request.isDeleted(), pageable);
        } else {
            page = taskRepository.findByCreatedByAndIsDeletedOrderByTitleDesc(
                    request.getCreatedBy(), request.isDeleted(), pageable);
        }

        log.info("Fetched {} tasks", page.getTotalElements());
        return page;
    }

    public TaskDTO getTaskById(String id) {
        log.info("Fetching task with ID: {}", id);
        return taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task not found with ID: {}", id);
                    return new EntityNotFoundException("Task not found with id: " + id);
                });
    }

    public TaskDTO updateTask(String id, TaskDTO updatedTask) {
        log.info("Updating task with ID: {}", id);
        TaskDTO existing = getTaskById(id);

        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setPriority(updatedTask.getPriority());
        existing.setDueDate(updatedTask.getDueDate());
        existing.setCompleted(updatedTask.isCompleted());

        TaskDTO saved = taskRepository.save(existing);
        log.info("Task with ID: {} successfully updated", id);
        return saved;
    }

    public TaskDTO switchCompletionStatus(String id) {
        log.info("Toggling completion status for task ID: {}", id);
        TaskDTO task = getTaskById(id);
        task.setCompleted(!task.isCompleted());
        TaskDTO saved = taskRepository.save(task);
        log.info("Task completion status updated to: {}", saved.isCompleted());
        return saved;
    }

    public void deleteTask(String id) {
        log.info("Permanently deleting task with ID: {}", id);
        taskRepository.deleteById(id);
    }

    public TaskDTO switchDeleteStatus(String id) {
        log.info("Toggling deleted status for task ID: {}", id);
        TaskDTO task = getTaskById(id);
        task.setDeleted(!task.isDeleted());
        TaskDTO saved = taskRepository.save(task);
        log.info("Task deleted status is now: {}", saved.isDeleted());
        return saved;
    }
}
