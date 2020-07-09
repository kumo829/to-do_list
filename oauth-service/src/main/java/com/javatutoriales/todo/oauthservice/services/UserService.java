package com.javatutoriales.todo.oauthservice.services;

import com.javatutoriales.todo.oauthservice.clients.UserClient;
import com.javatutoriales.todo.oauthservice.security.token.UserTokenEnhanced;
import com.javatutoriales.todo.users.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService, UserTokenEnhanced {

    private final UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Searching for user {}", username);

        return userClient.findByUsername(username)
                .map(OauthUser::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        return (User) loadUserByUsername(username);
    }

    private static class OauthUser extends User implements UserDetails {

        public OauthUser(@NotNull User user) {
            super(user);
            log.info("USer: {}", user);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return super.getRoles().stream().map(userRole ->
                    new SimpleGrantedAuthority("ROLE_" + userRole.getName()))
                    .peek(role -> log.info("Role: {}", role.getAuthority()))
                    .collect(Collectors.toList());
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }
    }
}
