package com.Teja.auth_service.service;

import com.Teja.auth_service.dto.LoginRequestDTO;
import com.Teja.auth_service.dto.LoginResponseDTO;
import com.Teja.auth_service.dto.RegisterRequestDTO;
import com.Teja.auth_service.dto.UserResponseDTO;

public interface AuthService {
    UserResponseDTO register(RegisterRequestDTO dto);

    LoginResponseDTO login(LoginRequestDTO dto);
}
