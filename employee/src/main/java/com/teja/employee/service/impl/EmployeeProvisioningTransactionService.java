package com.teja.employee.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teja.employee.AccountStatus;
import com.teja.employee.dto.EmployeeResponseDTO;
import com.teja.employee.dto.HrmsEvent;
import com.teja.employee.entity.Employee;
import com.teja.employee.entity.OutboxEvent;
import com.teja.employee.repository.EmployeeRepository;
import com.teja.employee.repository.OutboxEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeProvisioningTransactionService {
    private final EmployeeRepository employeeRepository;
    private final OutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public EmployeeResponseDTO activateAndCreateEvent(Employee employee) {

        employee.setAccountStatus(AccountStatus.ACTIVE);

        Employee updated = employeeRepository.save(employee);

        UUID eventId = UUID.randomUUID();

        HrmsEvent event = HrmsEvent.builder()
                .eventId(eventId)
                .eventType("EMPLOYEE_CREATED")
                .employeeId(updated.getEmployeeCode())
                .recipientEmail(employee.getEmail())
                .title("New Employee Created")
                .message(updated.getEmployeeName()
                        + " has joined the "
                        + updated.getDepartment()
                        + " department.")
                .occurredAt(Instant.now())
                .build();

        try {
            OutboxEvent outbox = OutboxEvent.builder()
                    .eventId(eventId.toString())
                    .topic("hrms-events")
                    .eventKey(updated.getEmployeeCode())
                    .payload(objectMapper.writeValueAsString(event))
                    .published(false)
                    .createdAt(Instant.now())
                    .build();

            outboxRepository.save(outbox);

        } catch (JsonProcessingException ex) {
            throw new IllegalStateException(
                    "Unable to serialize employee event", ex);
        }

        return EmployeeResponseDTO.builder()
                .id(updated.getId())
                .employeeCode(updated.getEmployeeCode())
                .employeeName(updated.getEmployeeName())
                .email(updated.getEmail())
                .department(updated.getDepartment())
                .designation(updated.getDesignation())
                .build();
    }
}
