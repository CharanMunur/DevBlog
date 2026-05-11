package com.devblog.server.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class UserResponse {
    private UUID id;
    private String username;
    private String name;
    private String bio;
    private String dateJoined;
}
