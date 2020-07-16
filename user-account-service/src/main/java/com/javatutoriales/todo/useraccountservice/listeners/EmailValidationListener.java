package com.javatutoriales.todo.useraccountservice.listeners;

import com.javatutoriales.todo.useraccountservice.events.EmailValidationEvent;
import com.javatutoriales.todo.users.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailValidationListener implements ApplicationListener<EmailValidationEvent> {
    private final JavaMailSender mailSender;

    @Value("${application.host}")
    private String host;


    @Override
    public void onApplicationEvent(EmailValidationEvent emailValidationEvent) {
        UserDto user = emailValidationEvent.getUser();

        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject("Email account verified");
        message.setText("You have successfully confirmed your account.");
        message.setTo(user.getEmail());

        mailSender.send(message);
    }
}
