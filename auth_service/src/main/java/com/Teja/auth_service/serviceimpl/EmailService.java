package com.Teja.auth_service.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendTemporaryPassword(
            String email,
            String username,
            String temporaryPassword) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Your HRMS Login Credentials");

        message.setText(
                "Hello,\n\n"
                        + "Your HRMS account has been created.\n\n"
                        + "Username: " + username + "\n"
                        + "Temporary Password: " + temporaryPassword + "\n\n"
                        + "Please log in and change your password immediately.\n"
                        + "This temporary password expires in 24 hours.\n\n"
                        + "Regards,\nHRMS Team"
        );

        mailSender.send(message);
    }
}
