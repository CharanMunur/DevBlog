package com.devblog.server.service;

import com.devblog.server.exception.PostNotFoundException;
import com.devblog.server.model.Like;
import com.devblog.server.model.LikeId;
import com.devblog.server.model.Post;
import com.devblog.server.model.User;
import com.devblog.server.repository.LikeRepository;
import com.devblog.server.repository.PostRepository;
import com.devblog.server.util.AuthUtils;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final AuthUtils authUtils;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    public LikeService(
        AuthUtils authUtils,
        LikeRepository likeRepository,
        PostRepository postRepository
    ) {
        this.authUtils = authUtils;
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
    }

    public void likePost(Long postId) {
        User user = authUtils.getCurrentUser();
        Post post = postRepository
            .findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId));

        Like like = new Like();
        like.setPost(post);
        like.setUser(user);

        likeRepository.save(like);
    }

    public void unLikePost(Long postId) {
        User currentUser = authUtils.getCurrentUser();
        LikeId likeId = new LikeId();
        likeId.setPostId(postId);
        likeId.setUserId(currentUser.getId());

        likeRepository.deleteById(likeId);
    }
}
