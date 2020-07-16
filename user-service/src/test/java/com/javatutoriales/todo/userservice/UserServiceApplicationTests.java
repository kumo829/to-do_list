package com.javatutoriales.todo.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
//@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
class UserServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
