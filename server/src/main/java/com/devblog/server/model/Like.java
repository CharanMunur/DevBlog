package com.devblog.server.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "likes")
@Data
public class Like {
    
    @EmbeddedId
    private LikeId id;

    @ManyToOne
    @MapsId("post")
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @MapsId("user")
    @JoinColumn(name = "user_id")
    private User user;
}
