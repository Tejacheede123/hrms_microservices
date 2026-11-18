package com.teja.employee.controller;

import com.teja.employee.dto.EmployeeRequestDTO;
import com.teja.employee.dto.EmployeeResponseDTO;
import com.teja.employee.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    public EmployeeResponseDTO createEmployee(
            @Valid
            @RequestBody EmployeeRequestDTO request) {

        return employeeService.createEmployee(
                request);
    }

    @GetMapping("/{id}")
    public EmployeeResponseDTO getEmployee(
            @PathVariable Long id) {

        return employeeService.getEmployeeById(id);
    }

    @GetMapping
    public List<EmployeeResponseDTO>
    getAllEmployees() {

        return employeeService.getAllEmployees();
    }

    @PutMapping("/{id}")
    public EmployeeResponseDTO updateEmployee(
            @PathVariable Long id,
            @Valid
            @RequestBody EmployeeRequestDTO request) {

        return employeeService.updateEmployee(
                id,
                request);
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(
            @PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return "Employee deleted successfully";
    }
}
