package com.thales.taskmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thales.taskmanager.dto.ApiResponse;
import com.thales.taskmanager.dto.User;
import com.thales.taskmanager.dto.UserRequest;
import com.thales.taskmanager.enums.Role;
import com.thales.taskmanager.service.UserService;

import static com.thales.taskmanager.utils.Constants.USER_CREATED;
import static com.thales.taskmanager.utils.Constants.USER_UPDATED;
import static com.thales.taskmanager.utils.Constants.USER_DELETED;
import static com.thales.taskmanager.utils.Constants.USERS_RETRIEVED;

@RestController
@RequestMapping("/api/users")
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
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        try {
            User created = userService.createUser(user);
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
     * Retrieves a list of all users.
     *
     * @return ApiResponse containing user list
     */
    @GetMapping("/getData")
    public ResponseEntity<ApiResponse<Page<User>>> getAllUsers(
            @RequestParam(required = false) Role role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.getUsers(pageable, role);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), USERS_RETRIEVED, users));
    }

    /**
     * Updates a user's role or password.
     *
     * @param username    the ID of the user to update
     * @param updatedUser the updated user data
     * @return ApiResponse with the updated user
     */
    @PutMapping("/update/{username}")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable String username,
            @RequestBody User updatedUser) {
        User user = userService.updateUser(username, updatedUser);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), USER_UPDATED, user));
    }

    /**
     * Deletes a user by their username.
     *
     * @param username the user to delete
     * @return ApiResponse with no data
     */
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), USER_DELETED, null));
    }
}
