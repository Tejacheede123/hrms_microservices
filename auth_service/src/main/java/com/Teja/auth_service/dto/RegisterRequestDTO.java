package com.Teja.auth_service.dto;

import com.Teja.auth_service.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {
    @NotBlank
    private String employeeId;

    @NotBlank
    private String username;

    @Email
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    private Role role;
}
