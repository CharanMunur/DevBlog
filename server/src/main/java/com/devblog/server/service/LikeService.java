package com.devblog.server.service;

import com.devblog.server.exception.PostNotFoundException;
import com.devblog.server.mapper.BlogMapper;
import com.devblog.server.model.Like;
import com.devblog.server.model.LikeId;
import com.devblog.server.model.Post;
import com.devblog.server.model.User;
import com.devblog.server.repository.LikeRepository;
import com.devblog.server.repository.PostRepository;
import com.devblog.server.util.AuthUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LikeService {

    private final AuthUtils authUtils;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final BlogMapper blogMapper;

    public LikeService(
        AuthUtils authUtils,
        LikeRepository likeRepository,
        PostRepository postRepository,
        BlogMapper blogMapper
    ) {
        this.authUtils = authUtils;
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.blogMapper = blogMapper;
    }

    public void likePost(Long postId) {
        User user = authUtils.getCurrentUser();
        Post post = postRepository
            .findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));
        LikeId likeId = blogMapper.toLikeId(postId, user.getId());

        if (likeRepository.existsById(likeId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already liked");
        }

        Like like = blogMapper.toLike(user, post);

        likeRepository.save(like);
    }

    public void unlikePost(Long postId) {
        User currentUser = authUtils.getCurrentUser();
        LikeId likeId = blogMapper.toLikeId(postId, currentUser.getId());

        likeRepository.deleteById(likeId);
    }
}
