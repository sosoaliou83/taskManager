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

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * Creates and saves a new task in the database.
     *
     * @param task the task to create
     * @return the saved Task object
     */
    public TaskDTO createTask(TaskDTO task) {
        task.setCreatedDate(LocalDate.now());
        return taskRepository.save(task);
    }

    /**
     * Retrieves a paginated list of tasks matching the given request.
     *
     * @param request a TaskRequest containing optional filters (priority, dueDate,
     *                deleted),
     *                the creator’s username, and pagination parameters (page, size)
     * @return a page of Task matching those criteria
     */
    public Page<TaskDTO> getTasks(TaskRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        if (request.getPriority() != null && request.getDueDate() != null) {
            return taskRepository.findByPriorityAndDueDateAndCreatedByAndIsDeletedOrderByTitleDesc(
                    request.getPriority(),
                    request.getDueDate(),
                    request.getCreatedBy(), request.isDeleted(), pageable);
        } else if (request.getPriority() != null) {
            return taskRepository.findByPriorityAndCreatedByAndIsDeletedOrderByTitleDesc(request.getPriority(),
                    request.getCreatedBy(), request.isDeleted(), pageable);
        } else if (request.getDueDate() != null) {
            return taskRepository.findByDueDateAndCreatedByAndIsDeletedOrderByTitleDesc(request.getDueDate(),
                    request.getCreatedBy(), request.isDeleted(), pageable);
        } else {
            return taskRepository.findByCreatedByAndIsDeletedOrderByTitleDesc(request.getCreatedBy(),
                    request.isDeleted(),
                    pageable);
        }
    }

    /**
     * Retrieves a single task by its ID.
     *
     * @param id the task ID
     * @return the task if found
     * @throws EntityNotFoundException if no task with the given ID exists
     */
    public TaskDTO getTaskById(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
    }

    /**
     * Updates an existing task with new data.
     *
     * @param id          the ID of the task to update
     * @param updatedTask the new task data
     * @return the updated Task object
     */
    public TaskDTO updateTask(String id, TaskDTO updatedTask) {
        TaskDTO existing = getTaskById(id);

        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setPriority(updatedTask.getPriority());
        existing.setDueDate(updatedTask.getDueDate());
        existing.setCompleted(updatedTask.isCompleted());

        return taskRepository.save(existing);
    }

    /**
     * Switch a task to 'Completed' status (true ↔ false).
     *
     * @param id the task ID
     * @return the updated Task object
     */
    public TaskDTO switchCompletionStatus(String id) {
        TaskDTO task = getTaskById(id);
        task.setCompleted(!task.isCompleted());
        return taskRepository.save(task);
    }

    /**
     * Permanently deletes a task by its ID.
     *
     * @param id the task ID
     */
    public void deleteTask(String id) {
        taskRepository.deleteById(id);
    }

    /**
     * Switch a task to "Deleted" status | soft delete or undo (true ↔ false).
     *
     * @param id the task ID
     */
    public TaskDTO switchDeleteStatus(String id) {
        TaskDTO task = getTaskById(id);
        task.setDeleted(!task.isDeleted());
        return taskRepository.save(task);
    }
}