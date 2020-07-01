package com.javatutoriales.todo.oauthservice.security.token;

import com.javatutoriales.todo.users.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserTokenEnhanced {
    User findByUsername(String username) throws UsernameNotFoundException;
}
