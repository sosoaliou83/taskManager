package com.thales.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.thales.taskmanager.dto.User;

public interface UserRepository extends JpaRepository<User, String> {
    // Optional: for login/security
    User findByUsername(String username);
}
