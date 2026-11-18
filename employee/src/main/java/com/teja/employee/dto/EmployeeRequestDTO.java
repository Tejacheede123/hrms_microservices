package com.teja.employee.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmployeeRequestDTO {
    @NotBlank
    private String employeeCode;

    @NotBlank
    private String employeeName;

    @Email
    private String email;

    private String department;

    private String designation;
}
