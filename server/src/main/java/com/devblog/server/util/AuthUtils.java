package com.devblog.server.util;

import com.devblog.server.model.User;
import com.devblog.server.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthUtils {

    private final UserRepository userRepository;

    public AuthUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        String email = authentication.getName();
        return userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
