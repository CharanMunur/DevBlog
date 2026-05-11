package com.devblog.server.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class LikeId implements Serializable{
    private Long post;
    private UUID user;
}
