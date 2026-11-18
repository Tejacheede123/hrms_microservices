package com.Teja.auth_service.controller;

import com.Teja.auth_service.dto.EmployeeUserProvisionRequestDTO;
import com.Teja.auth_service.dto.UserResponseDTO;
import com.Teja.auth_service.serviceimpl.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController {
    private final AuthServiceImpl authService;

    @PostMapping("/provision")
    public UserResponseDTO createEmployeeAccount(
            @Valid @RequestBody EmployeeUserProvisionRequestDTO dto) {

        return authService.createEmployeeAccount(dto);
    }
}
