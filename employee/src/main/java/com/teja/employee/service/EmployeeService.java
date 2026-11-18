package com.teja.employee.service;

import com.teja.employee.dto.EmployeeRequestDTO;
import com.teja.employee.dto.EmployeeResponseDTO;

import java.util.List;

public interface EmployeeService {
    EmployeeResponseDTO createEmployee(
            EmployeeRequestDTO request);

    EmployeeResponseDTO getEmployeeById(Long id);

    List<EmployeeResponseDTO> getAllEmployees();

    EmployeeResponseDTO updateEmployee(
            Long id,
            EmployeeRequestDTO request);

    void deleteEmployee(Long id);
}
