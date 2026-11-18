package com.leave.leaveservice.service;

import com.leave.leaveservice.dto.LeaveRequestDTO;
import com.leave.leaveservice.dto.LeaveResponseDTO;

import java.util.List;

public interface LeaveService {
    LeaveResponseDTO applyLeave(
            LeaveRequestDTO request);

   /* List<LeaveResponseDTO> getAllLeaves();

    LeaveResponseDTO getLeaveById(
            Long id);

    LeaveResponseDTO approveLeave(
            Long id);

    LeaveResponseDTO rejectLeave(
            Long id);*/
}
