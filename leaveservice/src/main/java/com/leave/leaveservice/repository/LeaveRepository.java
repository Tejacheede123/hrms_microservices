package com.leave.leaveservice.repository;

import com.leave.leaveservice.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRepository extends JpaRepository<LeaveRequest, Long> {

}
