package com.thales.taskmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.thales.taskmanager.dto.User;
import com.thales.taskmanager.enums.Role;

public interface UserRepository extends JpaRepository<User, String> {

    Page<User> findAll(Pageable pageable);

    Page<User> findByRole(Role role, Pageable pageable);
}
