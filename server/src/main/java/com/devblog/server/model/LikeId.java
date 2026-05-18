package com.devblog.server.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Embeddable
@Data
public class LikeId implements Serializable {

    private Long postId;
    private UUID userId;
}
