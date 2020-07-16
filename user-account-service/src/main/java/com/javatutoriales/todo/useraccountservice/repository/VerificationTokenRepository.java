package com.javatutoriales.todo.useraccountservice.repository;

import com.javatutoriales.todo.useraccountservice.model.Verification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends MongoRepository<Verification, String> {

}
