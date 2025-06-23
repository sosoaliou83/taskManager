package com.thales.taskmanager.controller;

import static com.thales.taskmanager.utils.Constants.USER_CREATED;
import static com.thales.taskmanager.utils.Constants.USER_DELETED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thales.taskmanager.dto.ApiResponse;
import com.thales.taskmanager.dto.UserDTO;
import com.thales.taskmanager.enums.Role;
import com.thales.taskmanager.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing users.
 * Provides endpoints for creating, reading, updating, deleting
 * users.
 */
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    /**
     * Creates a new user if the username does not already exist.
     *
     * @param user the user to create
     * @return ApiResponse with status and created user or error
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody UserDTO user) {
        try {
            UserDTO created = userService.createUser(user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(HttpStatus.CREATED.value(), USER_CREATED, created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        }
    }

    /**
     * Returns only the role of the specified user.
     *
     * @param username the username to look up
     */
    @GetMapping("/role")
    public ResponseEntity<ApiResponse<Role>> getUserRole(
            @RequestParam("username") String username) {
        UserDTO user = userService.getUser(username);
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            HttpStatus.NOT_FOUND.value(),
                            "User not found",
                            null));
        }
        // Return only the role
        Role role = user.getRole();
        return ResponseEntity.ok(
                new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "User role retrieved",
                        role));
    }
}
