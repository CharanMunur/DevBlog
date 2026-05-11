package com.devblog.server.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class LoginResponse {
    private UUID id;
    private String username;
    private String token;
}
