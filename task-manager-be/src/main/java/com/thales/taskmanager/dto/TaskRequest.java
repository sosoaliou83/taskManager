package com.thales.taskmanager.dto;

import com.thales.taskmanager.enums.Priority;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {
    private Priority priority;
    private LocalDate dueDate;
    private int page = 0;
    private int size = 10;
}
