package com.leave.leaveservice.dto;

import lombok.Data;

@Data
public class EmployeeResponseDTO {
    private Long id;

    private String employeeCode;

    private String employeeName;

    private String email;

    private String department;

    private String designation;
}
