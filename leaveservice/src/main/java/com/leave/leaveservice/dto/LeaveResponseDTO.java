package com.leave.leaveservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class LeaveResponseDTO {
    private Long id;
    private String employeeName;

    private Long employeeId;

    private String leaveType;

    private LocalDate fromDate;

    private LocalDate toDate;

    private String reason;

    private String status;
}
