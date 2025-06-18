package com.thales.taskmanager.dto;

import lombok.Data;

import com.thales.taskmanager.enums.Role;

@Data
public class UserRequest {
    private Role role;
    private int page = 0;
    private int size = 10;
}
