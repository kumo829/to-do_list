package com.javatutoriales.todo.useraccountservice.listeners;

import com.javatutoriales.todo.useraccountservice.events.UserRegistrationEvent;
import com.javatutoriales.todo.useraccountservice.services.VerificationService;
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
public class EmailVerificationListener implements ApplicationListener<UserRegistrationEvent> {

    private final JavaMailSender mailSender;
    private final VerificationService verificationService;

    @Value("${application.host}")
    private String host;


    @Override
    public void onApplicationEvent(UserRegistrationEvent event) {
        String username = event.getUser().getUsername();
        String email = event.getUser().getEmail();
        String name = event.getUser().getName();

        String verificationId = verificationService.generateVerificationId(username);

        String verificationText = String.format("Account verification link: %s?id=%s", host, verificationId);

        log.info("Verification text: {}", verificationText);

        try {
            mailSender.send(getMailMessage(email, "TO-DO lists account verification", verificationId, name));
        }catch(MessagingException ex){
            log.error("Error sending confirmation email", ex);
        }
    }

    private MimeMessage getMailMessage(String to, String subject, String verificationId, String name) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        String text = String.format("<h1>Welcome to TO-DO List</h1><p>Dear %s:</p>" +
                "<p>Thanks for creating your account. As a final step you need to validate your email address clicking the following link:</p>" +
                "<p><a href=\"%s?id=%s\">Verify my account</a></p>" +
                "<p>The link will be valid for two days.</p>", name, host, verificationId);

        helper.setText(text);

        return message;
    }
}
