package com.thales.taskmanager.service;

import org.springframework.stereotype.Service;

import com.thales.taskmanager.dto.UserDTO;
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
    public UserDTO createUser(UserDTO user) {
        if (userRepository.existsById(user.getUsername())) {
            throw new IllegalArgumentException("User already exists with username: " + user.getUsername());
        }
        return userRepository.save(user);
    }

    /**
     * Retrieves a single user by their username.
     * 
     * @param username the unique identifier of the user
     * @return the User object if found
     * @throws EntityNotFoundException if user does not exist
     */
    public UserDTO getUser(String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
    }

}
