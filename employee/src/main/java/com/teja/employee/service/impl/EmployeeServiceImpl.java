package com.teja.employee.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teja.employee.AccountStatus;
import com.teja.employee.client.AuthServiceClient;
import com.teja.employee.dto.EmployeeRequestDTO;
import com.teja.employee.dto.EmployeeResponseDTO;
import com.teja.employee.dto.EmployeeUserProvisionRequestDTO;
import com.teja.employee.dto.HrmsEvent;
import com.teja.employee.entity.Employee;
import com.teja.employee.entity.OutboxEvent;
import com.teja.employee.exception.ResourceNotFoundException;
import com.teja.employee.repository.EmployeeRepository;
import com.teja.employee.repository.OutboxEventRepository;
import com.teja.employee.service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl   implements EmployeeService {
    private final EmployeeRepository repository;
    private final AuthServiceClient authServiceClient;
    private final OutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final EmployeeProvisioningTransactionService
            provisioningTransactionService;

  /*  @Override
    public EmployeeResponseDTO createEmployee(
            EmployeeRequestDTO request) {

        Employee employee =
                Employee.builder()
                        .employeeCode(
                                request.getEmployeeCode())
                        .employeeName(
                                request.getEmployeeName())
                        .email(
                                request.getEmail())
                        .department(
                                request.getDepartment())
                        .designation(
                                request.getDesignation())
                        .build();

        return mapToResponse(
                repository.save(employee));
    }*/
 /* @Override
  public EmployeeResponseDTO createEmployee(
          EmployeeRequestDTO request) {

      // 1. Save employee in Employee Database
      Employee employee = Employee.builder()
              .employeeCode(request.getEmployeeCode())
              .employeeName(request.getEmployeeName())
              .email(request.getEmail())
              .department(request.getDepartment())
              .designation(request.getDesignation())
              .build();

      Employee savedEmployee = repository.save(employee);

      // 2. Prepare Auth Service request
      EmployeeUserProvisionRequestDTO authRequest =
              EmployeeUserProvisionRequestDTO.builder()
                      .employeeId(savedEmployee.getEmployeeCode())
                      .username(savedEmployee.getEmployeeCode())
                      .email(savedEmployee.getEmail())
                      .employeeName(savedEmployee.getEmployeeName())
                      .build();

      // 3. Call Auth Service using Feign
      authServiceClient.createUser(authRequest);

      // 4. Return employee response
      return mapToResponse(savedEmployee);
  }*/

    @Override
    public EmployeeResponseDTO createEmployee(
            EmployeeRequestDTO request) {

        // 1. Save employee with PENDING status
        Employee employee = Employee.builder()
                .employeeCode(request.getEmployeeCode())
                .employeeName(request.getEmployeeName())
                .email(request.getEmail())
                .department(request.getDepartment())
                .designation(request.getDesignation())
                .accountStatus(AccountStatus.PENDING)
                .build();

        Employee savedEmployee = repository.save(employee);

        try {

            // 2. Prepare Auth Service request
            EmployeeUserProvisionRequestDTO authRequest =
                    EmployeeUserProvisionRequestDTO.builder()
                            .employeeId(savedEmployee.getEmployeeCode())
                            .username(savedEmployee.getEmployeeCode())
                            .email(savedEmployee.getEmail())
                            .employeeName(savedEmployee.getEmployeeName())
                            .build();

            // 3. Create login account through Feign
            authServiceClient.createUser(authRequest);

            log.info("Auth account created for employee: {}",
                    savedEmployee.getEmployeeCode());

        } catch (Exception ex) {

            // 4. Mark employee provisioning as FAILED
            savedEmployee.setAccountStatus(AccountStatus.FAILED);
            repository.save(savedEmployee);

            log.error("Auth account creation failed for employee: {}",
                    savedEmployee.getEmployeeCode(), ex);

            throw new RuntimeException(
                    "Employee saved, but Auth account creation failed. "
                            + "Employee code: "
                            + savedEmployee.getEmployeeCode(), ex);
        }

        // 5. Activate employee and save Kafka event to outbox
     //   return activateEmployeeAndCreateOutboxEvent(savedEmployee);
        return provisioningTransactionService
                .activateAndCreateEvent(savedEmployee);
    }

    @Transactional
    public EmployeeResponseDTO activateEmployeeAndCreateOutboxEvent(
            Employee employee) {

        employee.setAccountStatus(AccountStatus.ACTIVE);

        Employee updatedEmployee = repository.save(employee);

        UUID eventId = UUID.randomUUID();

        HrmsEvent event = HrmsEvent.builder()
                .eventId(eventId)
                .eventType("EMPLOYEE_CREATED")
                .employeeId(updatedEmployee.getEmployeeCode())
                .title("New Employee Created")
                .message(updatedEmployee.getEmployeeName()
                        + " has joined the "
                        + updatedEmployee.getDepartment()
                        + " department.")
                .occurredAt(Instant.now())
                .build();

        try {

            String payload = objectMapper.writeValueAsString(event);

            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .eventId(eventId.toString())
                    .topic("hrms-events")
                    .eventKey(updatedEmployee.getEmployeeCode())
                    .payload(payload)
                    .published(false)
                    .createdAt(Instant.now())
                    .build();

            outboxRepository.save(outboxEvent);

        } catch (JsonProcessingException ex) {

            throw new RuntimeException(
                    "Failed to create employee Kafka event", ex);
        }

        return mapToResponse(updatedEmployee);
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(
            Long id) {

        Employee employee =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found with id : "
                                                + id));

        return mapToResponse(employee);
    }

    @Override
    public List<EmployeeResponseDTO>
    getAllEmployees() {

        return repository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public EmployeeResponseDTO updateEmployee(
            Long id,
            EmployeeRequestDTO request) {

        Employee employee =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found"));

        employee.setEmployeeCode(
                request.getEmployeeCode());

        employee.setEmployeeName(
                request.getEmployeeName());

        employee.setEmail(
                request.getEmail());

        employee.setDepartment(
                request.getDepartment());

        employee.setDesignation(
                request.getDesignation());

        return mapToResponse(
                repository.save(employee));
    }

    @Override
    public void deleteEmployee(Long id) {

        Employee employee =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found"));

        repository.delete(employee);
    }

    private EmployeeResponseDTO
    mapToResponse(Employee employee) {

        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .employeeCode(
                        employee.getEmployeeCode())
                .employeeName(
                        employee.getEmployeeName())
                .email(employee.getEmail())
                .department(
                        employee.getDepartment())
                .designation(
                        employee.getDesignation())
                .build();
    }
}
