package com.Teja.auth_service.dto;

import lombok.Data;

@Data
public class EmployeeUserProvisionRequestDTO {
    private String employeeId;
    private String username;
    private String email;
    private String employeeName;
}
