package com.devblog.server.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;

@Getter
public class RegisterResponse {
    private UUID id;
    private String username;
    private String name;
    private String email;
    private LocalDateTime dateJoined;
}
