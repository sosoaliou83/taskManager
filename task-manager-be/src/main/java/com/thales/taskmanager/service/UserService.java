package com.thales.taskmanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thales.taskmanager.dto.User;
import com.thales.taskmanager.enums.Role;
import com.thales.taskmanager.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Creates a new user or updates an existing one with the same username.
     * 
     * @param user the user to be created
     * @return the saved User object
     * @throws IllegalArgumentException if a user with the given username already
     *                                  exists
     */
    public User createUser(User user) {
        if (userRepository.existsById(user.getUsername())) {
            throw new IllegalArgumentException("User already exists with username: " + user.getUsername());
        }
        return userRepository.save(user);
    }

    /**
     * Retrieves a paginated list of users, optionally filtered by their role.
     *
     * @param pageable the pagination parameters (page number, size, sort)
     * @param role     optional role filter; if null, returns all users
     * @return a page of users matching the criteria
     */
    public Page<User> getUsers(Pageable pageable, Role role) {
        if (role != null) {
            return userRepository.findByRole(role, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }

    /**
     * Retrieves a single user by their username.
     * 
     * @param username the unique identifier of the user
     * @return the User object if found
     * @throws EntityNotFoundException if user does not exist
     */
    public User getUser(String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
    }

    /**
     * Updates the role and/or password of an existing user.
     * 
     * @param username    the user's ID
     * @param updatedUser the new data to apply
     * @return the updated User object
     */
    public User updateUser(String username, User updatedUser) {
        User existing = getUser(username);

        existing.setPassword(updatedUser.getPassword());
        existing.setRole(updatedUser.getRole());

        return userRepository.save(existing);
    }

    /**
     * Deletes a user by username. Throws an error if user does not exist.
     *
     * @param username the user's ID to be deleted
     * @throws EntityNotFoundException if user does not exist
     */
    public void deleteUser(String username) {
        if (!userRepository.existsById(username)) {
            throw new EntityNotFoundException("User not found: " + username);
        }
        userRepository.deleteById(username);
    }
}
