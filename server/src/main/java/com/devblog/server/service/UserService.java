package com.devblog.server.service;

import java.time.LocalDateTime;
import java.util.Collections;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devblog.server.dto.LoginRequest;
import com.devblog.server.dto.LoginResponse;
import com.devblog.server.dto.RegisterRequest;
import com.devblog.server.dto.RegisterResponse;
import com.devblog.server.model.User;
import com.devblog.server.repository.UserRepository;
import com.devblog.server.security.JwtUtil;


@Service
public class UserService implements UserDetailsService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

        public UserService(UserRepository userRepository, 
                       PasswordEncoder passwordEncoder, 
                       JwtUtil jwtUtil, 
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email:" + email));

        UserDetails springSecurityUser = org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password(user.getPassword())
        .authorities(Collections.emptyList())
        .build();

        return springSecurityUser;
    }

    public RegisterResponse register(RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDateJoined(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        RegisterResponse response = new RegisterResponse();

        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());
        response.setDateJoined(savedUser.getDateJoined());

        return response;
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        String token = jwtUtil.generateToken(user.getEmail());

        LoginResponse response = new LoginResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setToken(token);

        return response;
    }
}
