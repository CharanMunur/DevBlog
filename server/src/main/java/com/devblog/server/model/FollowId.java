package com.devblog.server.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class FollowId implements Serializable{
    private UUID follower;
    private UUID following;
}
