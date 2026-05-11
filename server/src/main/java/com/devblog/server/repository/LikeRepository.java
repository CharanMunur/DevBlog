package com.devblog.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devblog.server.model.Like;
import com.devblog.server.model.LikeId;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
    
}
