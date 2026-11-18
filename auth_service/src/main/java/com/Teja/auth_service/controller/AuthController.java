package com.Teja.auth_service.controller;

import com.Teja.auth_service.dto.LoginRequestDTO;
import com.Teja.auth_service.dto.LoginResponseDTO;
import com.Teja.auth_service.dto.RegisterRequestDTO;
import com.Teja.auth_service.dto.UserResponseDTO;
import com.Teja.auth_service.security.CustomUserDetails;
import com.Teja.auth_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication APIs", description = "User Registration, Login and Profile APIs")
public class AuthController {

    private final AuthService authService;

    // Register API
    @PostMapping("/register")
    @Operation(summary = "Register a new HRMS user")
    public ResponseEntity<UserResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO dto) {

        UserResponseDTO response = authService.register(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    // Login API
    @PostMapping("/login")
    @Operation(summary = "Login user and generate JWT token")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto) {

        LoginResponseDTO response = authService.login(dto);

        return ResponseEntity.ok(response);
    }

    // Profile API
    @GetMapping("/profile")
    @Operation(summary = "Get logged-in user profile")
    public ResponseEntity<UserResponseDTO> profile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserResponseDTO response = UserResponseDTO.builder()
                .employeeId(userDetails.getEmployeeId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .role(userDetails.getRole())
                .enabled(userDetails.isEnabled())
                .build();

        return ResponseEntity.ok(response);
    }
}
