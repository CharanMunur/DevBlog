package com.devblog.server.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "follows")
@Data
public class Follow {

    @EmbeddedId
    private FollowId id;
    
    @ManyToOne
    @MapsId("follower")
    @JoinColumn(name = "follower_id")
    private User follower_id;

    @ManyToOne
    @MapsId("following")
    @JoinColumn(name = "following_id")
    private User following_id;
}
