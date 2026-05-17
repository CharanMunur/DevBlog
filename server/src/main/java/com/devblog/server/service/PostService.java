package com.devblog.server.service;

import com.devblog.server.dto.PostRequest;
import com.devblog.server.dto.PostResponse;
import com.devblog.server.exception.PostNotFoundException;
import com.devblog.server.model.Post;
import com.devblog.server.model.User;
import com.devblog.server.repository.PostRepository;
import com.devblog.server.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public PostResponse createPost(PostRequest request) {
        User user = getCurrentUser();
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
        User user = getCurrentUser();

        Post existingPost = postRepository
            .findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));

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

    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new PostNotFoundException(id);
        }

        postRepository.deleteById(id);
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
