package com.devblog.server.service;

import com.devblog.server.dto.LoginRequest;
import com.devblog.server.dto.LoginResponse;
import com.devblog.server.dto.RegisterRequest;
import com.devblog.server.dto.RegisterResponse;
import com.devblog.server.dto.UserResponse;
import com.devblog.server.model.Follow;
import com.devblog.server.model.FollowId;
import com.devblog.server.model.User;
import com.devblog.server.repository.FollowRepository;
import com.devblog.server.repository.UserRepository;
import com.devblog.server.security.JwtUtil;
import com.devblog.server.util.AuthUtils;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FollowRepository followRepository;
    private final JwtUtil jwtUtil;
    private final AuthUtils authUtils;
    private final ObjectProvider<
        org.springframework.security.authentication.AuthenticationManager
    > authenticationManagerProvider;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtUtil jwtUtil,
        AuthUtils authUtils,
        ObjectProvider<
            org.springframework.security.authentication.AuthenticationManager
        > authenticationManagerProvider,
        FollowRepository followRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.followRepository = followRepository;
        this.jwtUtil = jwtUtil;
        this.authUtils = authUtils;
        this.authenticationManagerProvider = authenticationManagerProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository
            .findByEmail(email)
            .orElseThrow(() ->
                new UsernameNotFoundException("User not found with email: " + email)
            );

        UserDetails springSecurityUser =
            org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.emptyList())
                .build();

        return springSecurityUser;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already in use");
        }

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
        var authenticationManager = authenticationManagerProvider.getObject();
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() ->
                new UsernameNotFoundException("User not found with email: " + request.getEmail())
            );

        String token = jwtUtil.generateToken(user.getEmail());

        LoginResponse response = new LoginResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setToken(token);

        return response;
    }

    public UserResponse getProfile(String username) {
        User user = userRepository
            .findByUsername(username)
            .orElseThrow(() ->
                new UsernameNotFoundException("User not found with username : " + username)
            );

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setBio(user.getBio());
        response.setName(user.getName());
        response.setDateJoined(user.getDateJoined());

        return response;
    }

    public void followuser(UUID followingId) {
        User currentUser = authUtils.getCurrentUser();
        User followingUser = userRepository
            .findById(followingId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Follow follow = new Follow();

        follow.setFollower(currentUser);
        follow.setFollowing(followingUser);

        followRepository.save(follow);
    }

    public void unfollowUser(UUID followingId) {
        User currentUser = authUtils.getCurrentUser();
        FollowId followId = new FollowId();
        followId.setFollower(currentUser.getId());
        followId.setFollowing(followingId);

        followRepository.deleteById(followId);
    }

    public List<UserResponse> getFollowers(String username) {
        User user = userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Follow> follows = followRepository.findByFollowing(user);

        return follows
            .stream()
            .map(follow -> {
                UserResponse response = new UserResponse();
                response.setId(follow.getFollower().getId());
                response.setUsername(follow.getFollower().getUsername());
                response.setName(follow.getFollower().getName());
                response.setBio(follow.getFollower().getBio());
                response.setDateJoined(follow.getFollower().getDateJoined());

                return response;
            })
            .toList();
    }

    public List<UserResponse> getFollowing(String username) {
        User user = userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Follow> following = followRepository.findByFollower(user);

        return following
            .stream()
            .map(follow -> {
                UserResponse response = new UserResponse();
                response.setId(follow.getFollowing().getId());
                response.setUsername(follow.getFollowing().getUsername());
                response.setName(follow.getFollowing().getName());
                response.setBio(follow.getFollowing().getBio());
                response.setDateJoined(follow.getFollowing().getDateJoined());

                return response;
            })
            .toList();
    }
}
