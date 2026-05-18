package com.devblog.server.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private UUID id;
    private String username;
    private String name;
    private String bio;
    private LocalDateTime dateJoined;
}
