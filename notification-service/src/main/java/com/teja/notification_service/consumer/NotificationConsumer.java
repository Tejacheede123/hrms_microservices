package com.teja.notification_service.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teja.notification_service.dto.HrmsEvent;
import com.teja.notification_service.entity.Notification;
import com.teja.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
   /* private final NotificationRepository notificationRepository;

    @KafkaListener(
            topics = "hrms-events",
            groupId = "notification-group"
    )
    public void consumeEvent(HrmsEvent event) {

        log.info("Received Kafka event: {}", event);

        if (event == null
                || event.getEmployeeId() == null
                || event.getEventType() == null
                || event.getTitle() == null
                || event.getMessage() == null) {

            log.warn("Ignoring invalid HRMS event: {}", event);
            return;
        }

        Notification notification = Notification.builder()
                .employeeId(event.getEmployeeId())
                .eventType(event.getEventType())
                .title(event.getTitle())
                .message(event.getMessage())
                .read(false)
                .build();

        Notification savedNotification =
                notificationRepository.save(notification);

        log.info(
                "Notification saved successfully. Notification ID: {}, Employee ID: {}",
                savedNotification.getId(),
                savedNotification.getEmployeeId()
        );
    }*/

    private final ObjectMapper objectMapper;
    private final NotificationRepository notificationRepository;
    private final NotificationEmailService emailService;

  /*  @KafkaListener(
            topics = "hrms-events",
            groupId = "notification-group"
    )
    public void consumeEvent(String message) {

        try {
            log.info("Received Kafka message: {}", message);

            HrmsEvent event =
                    objectMapper.readValue(message, HrmsEvent.class);

            // 1. Save in-app notification
            Notification notification = Notification.builder()
                    .employeeId(event.getEmployeeId())
                    .eventType(event.getEventType())
                    .title(event.getTitle())
                    .message(event.getMessage())
                    .read(false)
                    .createdAt(Instant.now())
                    .build();

            notificationRepository.save(notification);

            log.info("In-app notification saved for employee: {}",
                    event.getEmployeeId());

            // 2. Send email notification
            emailService.sendNotificationEmail(
                    event.getRecipientEmail(),
                    event.getTitle(),
                    event.getMessage()
            );

            log.info("Notification email sent to: {}",
                    event.getRecipientEmail());

        } catch (Exception e) {
            log.error("Failed to process notification event", e);
            throw new RuntimeException(e);
        }
    }*/
  @KafkaListener(
          topics = "hrms-events",
          groupId = "notification-group"
  )
  public void consumeEvent(HrmsEvent event) {

      log.info("Received event: {}", event);

      // Save notification
      Notification notification = Notification.builder()
              .employeeId(event.getEmployeeId())
              .eventType(event.getEventType())
              .title(event.getTitle())
              .message(event.getMessage())
              .read(false)
              .createdAt(Instant.now())
              .build();

      notificationRepository.save(notification);

      log.info("Notification saved for employee: {}",
              event.getEmployeeId());

      // Send email
      emailService.sendNotificationEmail(
              event.getRecipientEmail(),
              event.getTitle(),
              event.getMessage()
      );

      log.info("Email sent to: {}", event.getRecipientEmail());
  }
}
