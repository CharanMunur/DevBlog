package com.devblog.server.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private UUID id;
    private String username;
    private String token;
}
