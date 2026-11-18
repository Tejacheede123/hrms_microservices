package com.leave.leaveservice.controller;

import com.leave.leaveservice.dto.LeaveRequestDTO;
import com.leave.leaveservice.dto.LeaveResponseDTO;
import com.leave.leaveservice.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping
    public LeaveResponseDTO applyLeave(
            @RequestBody LeaveRequestDTO request) {

        return leaveService.applyLeave(
                request);
    }
}
