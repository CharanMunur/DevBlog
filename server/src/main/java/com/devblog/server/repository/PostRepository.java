package com.devblog.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devblog.server.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    
}
