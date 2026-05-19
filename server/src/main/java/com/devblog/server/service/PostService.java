package com.devblog.server.service;

import com.devblog.server.dto.PostRequest;
import com.devblog.server.dto.PostResponse;
import com.devblog.server.exception.PostNotFoundException;
import com.devblog.server.model.Post;
import com.devblog.server.model.User;
import com.devblog.server.repository.PostRepository;
import com.devblog.server.util.AuthUtils;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final AuthUtils authUtils;

    public PostService(PostRepository postRepository, AuthUtils authUtils) {
        this.postRepository = postRepository;
        this.authUtils = authUtils;
    }

    public PostResponse createPost(PostRequest request) {
        User user = authUtils.getCurrentUser();
        Post post = new Post();

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(null);

        Post savedPost = postRepository.save(post);

        PostResponse response = new PostResponse();

        response.setId(savedPost.getId());
        response.setTitle(savedPost.getTitle());
        response.setContent(savedPost.getContent());
        response.setUsername(savedPost.getUser().getUsername());
        response.setCreatedAt(savedPost.getCreatedAt());

        return response;
    }

    public PostResponse editPost(Long postId, PostRequest request) {
        User user = authUtils.getCurrentUser();

        Post existingPost = postRepository
            .findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));

        if (!existingPost.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't own this post");
        }

        existingPost.setTitle(request.getTitle());
        existingPost.setContent(request.getContent());
        existingPost.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(existingPost);

        PostResponse response = new PostResponse();
        response.setId(savedPost.getId());
        response.setTitle(savedPost.getTitle());
        response.setContent(savedPost.getContent());
        response.setUsername(user.getUsername());
        response.setCreatedAt(existingPost.getCreatedAt());
        response.setUpdatedAt(savedPost.getUpdatedAt());

        return response;
    }

    public void deletePost(Long postId) {
        Post post = postRepository
            .findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));

        if (!post.getUser().getId().equals(authUtils.getCurrentUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't own this post");
        }
        postRepository.deleteById(postId);
    }

    public Page<PostResponse> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Post> posts = postRepository.findAll(pageable);

        return posts.map(post -> {
            PostResponse response = new PostResponse();
            response.setId(post.getId());
            response.setTitle(post.getTitle());
            response.setContent(post.getContent());
            response.setUsername(post.getUser().getUsername());
            response.setCreatedAt(post.getCreatedAt());
            response.setUpdatedAt(post.getUpdatedAt());

            return response;
        });
    }

    public PostResponse getPostById(Long postId) {
        Post post = postRepository
            .findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));

        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setUsername(post.getUser().getUsername());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());

        return response;
    }

    public Page<PostResponse> searchPost(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Post> posts = postRepository.findByTitleContainingIgnoreCase(query, pageable);

        return posts.map(post -> {
            PostResponse response = new PostResponse();
            response.setId(post.getId());
            response.setTitle(post.getTitle());
            response.setContent(post.getContent());
            response.setUsername(post.getUser().getUsername());
            response.setCreatedAt(post.getCreatedAt());
            response.setUpdatedAt(post.getUpdatedAt());

            return response;
        });
    }
}
