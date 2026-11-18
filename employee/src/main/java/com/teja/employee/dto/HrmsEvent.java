package com.teja.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HrmsEvent {
    private UUID eventId;
    private String eventType;
    private String employeeId;
    private String recipientEmail;
    private String title;
    private String message;
    private Instant occurredAt;
}
