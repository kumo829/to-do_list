package com.javatutoriales.todo.useraccountservice.web.controller;

import com.javatutoriales.todo.useraccountservice.clients.UserClient;
import com.javatutoriales.todo.useraccountservice.services.VerificationService;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/verify")
public class VerificationController {
    private final VerificationService verificationService;
    private final UserClient userClient;

    @Value("${application.loginUrl}")
    private String loginUrl;

    @GetMapping("/email")
    public ResponseEntity<Void> verifyEmail(@RequestParam String id) {
        verificationService.setVerificationIdUsed(id);

        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).header(HttpHeaders.LOCATION, loginUrl).build();
    }
}
