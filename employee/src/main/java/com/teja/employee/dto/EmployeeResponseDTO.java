package com.teja.employee.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeResponseDTO {
    private Long id;

    private String employeeCode;

    private String employeeName;

    private String email;

    private String department;

    private String designation;
}
