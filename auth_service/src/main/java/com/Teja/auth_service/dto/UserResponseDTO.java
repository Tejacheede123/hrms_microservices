package com.Teja.auth_service.dto;

import com.Teja.auth_service.enums.Role;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;

    private String employeeId;

    private String username;

    private String email;

    private Role role;

    private Boolean enabled;
}
