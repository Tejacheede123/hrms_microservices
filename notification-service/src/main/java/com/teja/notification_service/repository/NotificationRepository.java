package com.teja.notification_service.repository;

import com.teja.notification_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository     extends JpaRepository<Notification, Long> {
    // Get all notifications for an employee, newest first
    List<Notification> findByEmployeeIdOrderByCreatedAtDesc(
            String employeeId
    );

    // Get unread notifications for an employee
    List<Notification> findByEmployeeIdAndReadFalseOrderByCreatedAtDesc(
            String employeeId
    );

    // Count unread notifications
   // long countByEmployeeIdAndReadFalse(Long employeeId);
    long countByEmployeeIdAndReadFalse(String employeeId);
}
