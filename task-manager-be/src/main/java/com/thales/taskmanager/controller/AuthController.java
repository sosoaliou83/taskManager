package com.thales.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thales.taskmanager.dto.AuthenticationRequest;
import com.thales.taskmanager.dto.AuthenticationResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * Authenticates a user using username and password credentials.
     * If successful, returns a success message. TODO implement JWT if enough time
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));

            // TODO If have time, switch from basic auth to jwt
            log.info("User {} logged in successfully.", request.getUsername());
            return ResponseEntity.ok(new AuthenticationResponse("Login success"));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body(new AuthenticationResponse("Invalid username or password"));
        }
    }
}
