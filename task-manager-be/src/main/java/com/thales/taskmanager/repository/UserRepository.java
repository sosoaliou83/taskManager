package com.thales.taskmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.thales.taskmanager.dto.UserDTO;
import com.thales.taskmanager.enums.Role;

public interface UserRepository extends JpaRepository<UserDTO, String> {

    Page<UserDTO> findAll(Pageable pageable);

    Page<UserDTO> findByRole(Role role, Pageable pageable);
}
