package com.thales.taskmanager.service;

import com.thales.taskmanager.dto.Task;
import com.thales.taskmanager.enums.Priority;
import com.thales.taskmanager.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Page<Task> getTasks(Priority priority, LocalDate dueDate, Pageable pageable) {
        if (priority != null && dueDate != null) {
            return taskRepository.findByPriorityAndDueDate(priority, dueDate, pageable);
        } else if (priority != null) {
            return taskRepository.findByPriority(priority, pageable);
        } else if (dueDate != null) {
            return taskRepository.findByDueDate(dueDate, pageable);
        } else {
            return taskRepository.findAll(pageable);
        }
    }

    public Task getTaskById(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
    }

    public Task updateTask(String id, Task updatedTask) {
        Task existing = getTaskById(id);

        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setPriority(updatedTask.getPriority());
        existing.setDueDate(updatedTask.getDueDate());
        existing.setCompleted(updatedTask.isCompleted());

        return taskRepository.save(existing);
    }

    public Task switchCompletionStatus(String id) {
        Task task = getTaskById(id);
        task.setCompleted(!task.isCompleted());
        return taskRepository.save(task);
    }

    public void deleteTask(String id) {
        taskRepository.deleteById(id);
    }

    public void switchDeleteStatus(String id) {
        Task task = getTaskById(id);
        task.setDeleted(!task.isDeleted());
        taskRepository.save(task);
    }
}
