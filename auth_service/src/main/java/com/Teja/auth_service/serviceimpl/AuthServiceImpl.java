package com.Teja.auth_service.serviceimpl;

import com.Teja.auth_service.config.JwtUtil;
import com.Teja.auth_service.dto.*;
import com.Teja.auth_service.entity.User;
import com.Teja.auth_service.enums.Role;
import com.Teja.auth_service.exception.InvalidCredentialsException;
import com.Teja.auth_service.exception.ResourceAlreadyExistsException;
import com.Teja.auth_service.repository.UserRepository;
import com.Teja.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;
    private final TemporaryPasswordGenerator temporaryPasswordGenerator;
    private final EmailService emailService;

    // ---------------- REGISTER ----------------

    @Override
    public UserResponseDTO register(RegisterRequestDTO dto) {

        // Employee ID Already Exists?
        if (userRepository.existsByEmployeeId(dto.getEmployeeId())) {
            throw new ResourceAlreadyExistsException(
                    "Employee ID already exists.");
        }

        // Username Already Exists?
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResourceAlreadyExistsException(
                    "Username already exists.");
        }

        // Email Already Exists?
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException(
                    "Email already exists.");
        }

        User user = User.builder()
                .employeeId(dto.getEmployeeId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    // ---------------- LOGIN ----------------

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getUsername(),
                            dto.getPassword()));

        } catch (BadCredentialsException ex) {

            throw new InvalidCredentialsException(
                    "Invalid username or password.");
        }

        User user = userRepository
                .findByUsername(dto.getUsername())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "User not found."));

        String token = jwtUtil.generateToken(user);

        return LoginResponseDTO.builder()
                .token(token)
                .employeeId(user.getEmployeeId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }
    public UserResponseDTO createEmployeeAccount(
            EmployeeUserProvisionRequestDTO dto) {

        if (userRepository.existsByEmployeeId(dto.getEmployeeId())) {
            throw new ResourceAlreadyExistsException(
                    "Employee ID already exists.");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResourceAlreadyExistsException(
                    "Username already exists.");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException(
                    "Email already exists.");
        }

        String temporaryPassword = temporaryPasswordGenerator.generate();

        User user = User.builder()
                .employeeId(dto.getEmployeeId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(temporaryPassword))
                .role(Role.EMPLOYEE)
                .enabled(true)
                .passwordChangeRequired(true)
                .temporaryPasswordExpiresAt(
                        java.time.Instant.now().plusSeconds(86400))
                .build();

        User savedUser = userRepository.save(user);

        // Send the temporary password to the employee's email.
        emailService.sendTemporaryPassword(
                savedUser.getEmail(),
                savedUser.getUsername(),
                temporaryPassword
        );

        return mapToResponse(savedUser);
    }

    // ---------------- ENTITY TO DTO ----------------

    private UserResponseDTO mapToResponse(User user) {

        return UserResponseDTO.builder()
                .id(user.getId())
                .employeeId(user.getEmployeeId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .build();
    }
}
