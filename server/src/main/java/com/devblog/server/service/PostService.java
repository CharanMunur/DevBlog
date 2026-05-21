package com.devblog.server.service;

import com.devblog.server.dto.PostRequest;
import com.devblog.server.dto.PostResponse;
import com.devblog.server.exception.PostNotFoundException;
import com.devblog.server.mapper.BlogMapper;
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
    private final BlogMapper blogMapper;

    public PostService(PostRepository postRepository, AuthUtils authUtils, BlogMapper blogMapper) {
        this.postRepository = postRepository;
        this.authUtils = authUtils;
        this.blogMapper = blogMapper;
    }

    public PostResponse createPost(PostRequest request) {
        User user = authUtils.getCurrentUser();
        Post post = blogMapper.toNewPost(request, user, LocalDateTime.now());

        Post savedPost = postRepository.save(post);
        return blogMapper.toPostResponse(savedPost);
    }

    public PostResponse editPost(Long postId, PostRequest request) {
        User user = authUtils.getCurrentUser();

        Post existingPost = postRepository
            .findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));

        if (!existingPost.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't own this post");
        }

        Post updatedPost = blogMapper.updatePost(existingPost, request, LocalDateTime.now());

        Post savedPost = postRepository.save(updatedPost);
        return blogMapper.toPostResponse(savedPost);
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

        return posts.map(blogMapper::toPostResponse);
    }

    public PostResponse getPostById(Long postId) {
        Post post = postRepository
            .findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));

        return blogMapper.toPostResponse(post);
    }

    public Page<PostResponse> searchPost(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Post> posts = postRepository.findByTitleContainingIgnoreCase(query, pageable);

        return posts.map(blogMapper::toPostResponse);
    }
}
