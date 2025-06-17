package com.thales.taskmanager.controller;

import com.thales.taskmanager.dto.Task;
import com.thales.taskmanager.dto.TaskRequest;
import com.thales.taskmanager.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;

    // Create a new task
    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    // Get tasks (with pagination + optional filters)
    @GetMapping("/getData")
    public ResponseEntity<Page<Task>> getTasks(@RequestBody TaskRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        return ResponseEntity.ok(taskService.getTasks(request.getPriority(), request.getDueDate(), pageable));
    }

    // Update task
    @PutMapping("/update/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable String id, @RequestBody Task task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    // Toggle completion status
    @PatchMapping("/toggle-completion/{id}")
    public ResponseEntity<Task> switchCompletionStatus(@PathVariable String id) {
        return ResponseEntity.ok(taskService.switchCompletionStatus(id));
    }

    // Toggle deletion status (soft delete / undo)
    @PatchMapping("/{id}/toggle-delete")
    public ResponseEntity<Void> switchDeleteStatus(@PathVariable String id) {
        taskService.switchDeleteStatus(id);
        return ResponseEntity.noContent().build();
    }

    // Hard delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}
