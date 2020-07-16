package com.javatutoriales.todo.useraccountservice.services;

import com.javatutoriales.todo.useraccountservice.model.Verification;

public interface VerificationService {
    String getUsernameForId(String id);

    String generateVerificationId(String username);

    void setVerificationIdUsed(String id);
}
