package com.leave.leaveservice.feign;

import com.leave.leaveservice.dto.EmployeeResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employee")
public interface EmployeeClient {
    @GetMapping("/api/employees/{id}")
    EmployeeResponseDTO getEmployeeById(
            @PathVariable("id") Long id);
}
