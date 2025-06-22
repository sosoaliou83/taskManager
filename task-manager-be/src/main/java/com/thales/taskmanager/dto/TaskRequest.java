package com.thales.taskmanager.dto;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thales.taskmanager.enums.Priority;
import lombok.Data;

@Data
public class TaskRequest {
    private Priority priority;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    private String createdBy;
    private boolean deleted;
    private Integer page = 0;
    private Integer size = 10;
}