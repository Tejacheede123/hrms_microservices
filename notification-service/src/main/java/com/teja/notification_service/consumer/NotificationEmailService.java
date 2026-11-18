package com.teja.notification_service.consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class NotificationEmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendNotificationEmail(
            String toEmail,
            String title,
            String message) {

        if (toEmail == null || toEmail.isBlank()) {
            throw new IllegalArgumentException(
                    "Employee email address is missing");
        }

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom(fromEmail);
        mail.setTo(toEmail);
        mail.setSubject("HRMS Notification: " + title);
        mail.setText(
                "Hello,\n\n" +
                        message +
                        "\n\nRegards,\nHRMS Team"
        );

        mailSender.send(mail);
    }
}
