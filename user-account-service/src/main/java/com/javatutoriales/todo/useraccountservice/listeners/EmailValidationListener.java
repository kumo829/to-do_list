package com.javatutoriales.todo.useraccountservice.listeners;

import com.javatutoriales.todo.useraccountservice.events.EmailValidationEvent;
import com.javatutoriales.todo.users.dto.UserDto;
import com.javatutoriales.todo.users.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailValidationListener implements ApplicationListener<EmailValidationEvent> {
    private final JavaMailSender mailSender;

    @Value("${application.host}")
    private String host;


    @Override
    public void onApplicationEvent(EmailValidationEvent emailValidationEvent) {
        UserDto user = emailValidationEvent.getUser();

        try {
            mailSender.send(getMailMessage(user.getEmail(), "Welcome to TO-DO Lists", user));
        } catch (MessagingException e) {
            log.error("Error sending confirmation email", e);
        }
    }

    private MimeMessage getMailMessage(String to, String subject, UserDto user) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        String text = String.format("<h1>Email account verified</h1>" +
                "<p>Dear %s:</p>" +
                "<p>You have successfully verified your account.</p>" +
                "<p>Your username is <strong>%s</strong></p>", user.getName(), user.getUsername());

        helper.setText(text, true);

        return message;
    }
}
