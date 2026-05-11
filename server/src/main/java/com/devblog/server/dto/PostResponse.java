package com.devblog.server.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;

@Getter
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private UUID username;
    private LocalDateTime createdAt;
}
