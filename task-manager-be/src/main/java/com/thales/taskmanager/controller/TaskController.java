package com.thales.taskmanager.controller;

import static com.thales.taskmanager.utils.Constants.TASKS_RETRIEVED;
import static com.thales.taskmanager.utils.Constants.TASK_COMPLETION_TOGGLED;
import static com.thales.taskmanager.utils.Constants.TASK_CREATED;
import static com.thales.taskmanager.utils.Constants.TASK_DELETED;
import static com.thales.taskmanager.utils.Constants.TASK_DELETION_TOGGLED;
import static com.thales.taskmanager.utils.Constants.TASK_UPDATED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thales.taskmanager.dto.ApiResponse;
import com.thales.taskmanager.dto.TaskDTO;
import com.thales.taskmanager.dto.TaskRequest;
import com.thales.taskmanager.service.TaskService;

import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing tasks.
 * Provides endpoints for creating, reading, updating, deleting, and toggling
 * task states.
 */
@RestController
@RequestMapping("/api/tasks")
@Slf4j
public class TaskController {

    @Autowired
    TaskService taskService;

    /**
     * Creates a new task.
     *
     * @param task the task object to create
     * @return the created task
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TaskDTO>> createTask(@RequestBody TaskDTO task) {
        TaskDTO created = taskService.createTask(task);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.CREATED.value(), TASK_CREATED, created));
    }

    /**
     * Retrieves a paginated list of tasks, optionally filtered by priority and due
     * date.
     *
     * @param request filter and pagination parameters
     * @return a page of filtered tasks
     */
    @GetMapping("/getData")
    public ResponseEntity<ApiResponse<Page<TaskDTO>>> getTasks(
            @ModelAttribute TaskRequest request) {
        Page<TaskDTO> tasks = taskService.getTasks(request);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), TASKS_RETRIEVED, tasks));
    }

    /**
     * Updates an existing task by ID.
     *
     * @param id   the ID of the task to update
     * @param task the updated task data
     * @return the updated task
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<TaskDTO>> updateTask(@PathVariable String id, @RequestBody TaskDTO task) {
        TaskDTO updated = taskService.updateTask(id, task);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), TASK_UPDATED, updated));
    }

    /**
     * Toggles the completion status of a task.
     *
     * @param id the ID of the task to toggle
     * @return the updated task with toggled completion state
     */
    @PatchMapping("/toggle-completion/{id}")
    public ResponseEntity<ApiResponse<TaskDTO>> switchCompletionStatus(@PathVariable String id) {
        TaskDTO task = taskService.switchCompletionStatus(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), TASK_COMPLETION_TOGGLED, task));
    }

    /**
     * Toggles the soft-deleted status of a task (mark as deleted or undo).
     *
     * @param id the ID of the task to toggle
     * @return empty response with status 200
     */
    @PatchMapping("/toggle-delete/{id}")
    public ResponseEntity<ApiResponse<TaskDTO>> switchDeleteStatus(@PathVariable String id) {
        TaskDTO task = taskService.switchDeleteStatus(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), TASK_DELETION_TOGGLED, task));
    }

    /**
     * Permanently deletes a task by ID.
     *
     * @param id the ID of the task to delete
     * @return empty response with status 200
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), TASK_DELETED, null));
    }
}
