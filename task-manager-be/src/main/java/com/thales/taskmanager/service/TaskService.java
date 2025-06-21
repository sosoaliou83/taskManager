package com.thales.taskmanager.service;

import com.thales.taskmanager.dto.TaskDTO;
import com.thales.taskmanager.enums.Priority;
import com.thales.taskmanager.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
        task.setCreatedDate(LocalDateTime.now());
        return taskRepository.save(task);
    }

    /**
     * Retrieves a paginated list of tasks, optionally filtered by priority and/or
     * due date.
     *
     * @param priority the priority to filter by (optional)
     * @param dueDate  the due date to filter by (optional)
     * @param pageable pagination and sorting information
     * @return a page of tasks matching the filters
     */
    public Page<TaskDTO> getTasks(Priority priority, LocalDate dueDate, String createdBy, Pageable pageable) {
        if (priority != null && dueDate != null) {
            return taskRepository.findByPriorityAndDueDateAndCreatedBy(priority, dueDate, createdBy, pageable);
        } else if (priority != null) {
            return taskRepository.findByPriorityAndCreatedBy(priority, createdBy, pageable);
        } else if (dueDate != null) {
            return taskRepository.findByDueDateAndCreatedBy(dueDate, createdBy, pageable);
        } else {
            return taskRepository.findByCreatedBy(createdBy, pageable);
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