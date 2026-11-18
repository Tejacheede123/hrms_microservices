package com.teja.employee.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeUserProvisionRequestDTO {
    private String employeeId;
    private String username;
    private String email;
    private String employeeName;
}
