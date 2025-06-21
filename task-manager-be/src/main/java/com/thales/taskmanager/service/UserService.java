package com.thales.taskmanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thales.taskmanager.dto.UserDTO;
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
    public UserDTO createUser(UserDTO user) {
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
    public Page<UserDTO> getUsers(Pageable pageable, Role role) {
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
    public UserDTO getUser(String username) {
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
    public UserDTO updateUser(String username, UserDTO updatedUser) {
        UserDTO existing = getUser(username);

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

    // /**
    // * Loads a user from the database and returns their credentials and roles.
    // *
    // * @param username the username to look up
    // * @return the user's details for authentication
    // * @throws UsernameNotFoundException if the user doesn't exist
    // */
    // @Override
    // public UserDetails loadUserByUsername(String username) throws
    // UsernameNotFoundException {
    // UserDTO user = userRepository.findById(username)
    // .orElseThrow(() -> new UsernameNotFoundException("User not found: " +
    // username));

    // return User
    // .withUsername(user.getUsername())
    // .password(user.getPassword()) // TODO : encode password
    // .roles(user.getRole().name())
    // .build();
    // }
}
