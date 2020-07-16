package com.javatutoriales.todo.useraccountservice.services;

import com.javatutoriales.todo.useraccountservice.clients.UserClient;
import com.javatutoriales.todo.useraccountservice.events.EmailValidationEvent;
import com.javatutoriales.todo.useraccountservice.model.Verification;
import com.javatutoriales.todo.useraccountservice.repository.VerificationTokenRepository;
import com.javatutoriales.todo.users.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {
    private final VerificationTokenRepository verificationRepository;
    private final UserClient userClient;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${application.validationTokenValidDays}")
    private int validDays;

    @Override
    public String getUsernameForId(String id) {
        Optional<Verification> verification = verificationRepository.findById(id);

        if (verification.isPresent()) {
            if (verification.get().isUsed()) {
                throw new IllegalStateException("verification link has already being used");
            }
            return verification.get().getUsername();
        }

        return null;
    }

    @Override
    public String generateVerificationId(String username) {
        Verification verification = new Verification(username);
        return verificationRepository.save(verification).getId();
    }

    @Override
    public void setVerificationIdUsed(String id) {
        Optional<Verification> optionalVerification = verificationRepository.findById(id);

        if (optionalVerification.isPresent()) {
            Verification verification = optionalVerification.get();

            if (verification.isUsed()) {
                throw new IllegalStateException("verification link has already being used");
            }

            if(isTokenExpired(verification)){
                throw new IllegalStateException("verification link has expired");
            }

            Optional<UserDto> user = userClient.validateUserEmail(verification.getUsername());
            eventPublisher.publishEvent(new EmailValidationEvent(user.get()));

            verification.setUsed(true);
            verification.setActivationDate(LocalDateTime.now());
            verificationRepository.save(verification);
        }
    }

    private boolean isTokenExpired(@NotNull Verification verification){
        LocalDateTime validLimitDate = LocalDateTime.now().minusDays(validDays);

        return verification.getCreatedDate().isBefore(validLimitDate);
    }
}
