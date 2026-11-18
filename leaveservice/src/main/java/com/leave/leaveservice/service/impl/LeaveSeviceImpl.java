package com.leave.leaveservice.service.impl;

import com.leave.leaveservice.dto.EmployeeResponseDTO;
import com.leave.leaveservice.dto.LeaveRequestDTO;
import com.leave.leaveservice.dto.LeaveResponseDTO;
import com.leave.leaveservice.entity.LeaveRequest;
import com.leave.leaveservice.exception.ServiceUnavailableException;
import com.leave.leaveservice.feign.EmployeeClient;
import com.leave.leaveservice.repository.LeaveRepository;
import com.leave.leaveservice.service.LeaveService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class LeaveSeviceImpl implements LeaveService {
    private final LeaveRepository repository;

    private final EmployeeClient employeeClient;
   /* @Override
    public LeaveResponseDTO applyLeave(
            LeaveRequestDTO request) {

        LeaveRequest leave =
                LeaveRequest.builder()
                        .employeeId(
                                request.getEmployeeId())
                        .leaveType(
                                request.getLeaveType())
                        .fromDate(
                                request.getFromDate())
                        .toDate(
                                request.getToDate())
                        .reason(
                                request.getReason())
                        .status("PENDING")
                        .build();

        return map(repository.save(leave));
    }*/
   @Override
   @CircuitBreaker(
           name = "employeeService",
           fallbackMethod = "employeeFallback")
   public LeaveResponseDTO applyLeave(
           LeaveRequestDTO dto) {

       EmployeeResponseDTO employee =
               employeeClient.getEmployeeById(
                       dto.getEmployeeId());

       if (employee == null) {
           throw new RuntimeException("Employee not found");
       }

       LeaveRequest leave =
               LeaveRequest.builder()
                       .employeeId(dto.getEmployeeId())
                       .leaveType(dto.getLeaveType())
                       .fromDate(dto.getFromDate())
                       .toDate(dto.getToDate())
                       .reason(dto.getReason())
                       .status("PENDING")
                       .build();

       return map(repository.save(leave));
   }
   //note :In real projects, we usually don't keep the fallback inside LeaveServiceImpl.Instead we move it into the Feign layer.
    /*
    @FeignClient(
        name = "EMPLOYEE-SERVICE",
        fallback = EmployeeClientFallback.class)
public interface EmployeeClient {

    @GetMapping("/api/employees/{id}")
    EmployeeResponseDTO getEmployeeById(
            @PathVariable Long id);
}
@Component
public class EmployeeClientFallback
        implements EmployeeClient {

    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {

        throw new ServiceUnavailableException(
                "Employee Service is unavailable");
    }

}
     */
    public LeaveResponseDTO employeeFallback(
            LeaveRequestDTO dto,
            Exception ex) {

        throw new ServiceUnavailableException(
                "Employee Service is currently unavailable. Please try again later.");
    }
    private LeaveResponseDTO map(LeaveRequest leave) {

        EmployeeResponseDTO employee =
                employeeClient.getEmployeeById(
                        leave.getEmployeeId());

        return LeaveResponseDTO.builder()
                .id(leave.getId())
                .employeeId(leave.getEmployeeId())
                .employeeName(employee.getEmployeeName())
                .leaveType(leave.getLeaveType())
                .fromDate(leave.getFromDate())
                .toDate(leave.getToDate())
                .reason(leave.getReason())
                .status(leave.getStatus())
                .build();
    }
}
