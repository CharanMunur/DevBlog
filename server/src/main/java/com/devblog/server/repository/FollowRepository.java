package com.devblog.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devblog.server.model.Follow;
import com.devblog.server.model.FollowId;

public interface FollowRepository extends JpaRepository<Follow, FollowId>{
    
}
