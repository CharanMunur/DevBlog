package com.devblog.server.mapper;

import com.devblog.server.dto.LoginResponse;
import com.devblog.server.dto.PostRequest;
import com.devblog.server.dto.PostResponse;
import com.devblog.server.dto.RegisterRequest;
import com.devblog.server.dto.RegisterResponse;
import com.devblog.server.dto.UserResponse;
import com.devblog.server.model.Follow;
import com.devblog.server.model.FollowId;
import com.devblog.server.model.Like;
import com.devblog.server.model.LikeId;
import com.devblog.server.model.Post;
import com.devblog.server.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class BlogMapper {

    public PostResponse toPostResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setUsername(post.getUser().getUsername());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        return response;
    }

    public RegisterResponse toRegisterResponse(User user) {
        RegisterResponse response = new RegisterResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setDateJoined(user.getDateJoined());
        return response;
    }

    public LoginResponse toLoginResponse(User user, String token) {
        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setToken(token);
        return response;
    }

    public UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setBio(user.getBio());
        response.setDateJoined(user.getDateJoined());
        return response;
    }

    public List<UserResponse> toUserResponses(List<User> users) {
        return users.stream().map(this::toUserResponse).toList();
    }

    public User toRegisteredUser(
        RegisterRequest request,
        String encodedPassword,
        LocalDateTime dateJoined
    ) {
        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setDateJoined(dateJoined);
        return user;
    }

    public Post toNewPost(PostRequest request, User user, LocalDateTime createdAt) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setUser(user);
        post.setCreatedAt(createdAt);
        post.setUpdatedAt(null);
        return post;
    }

    public Post updatePost(Post post, PostRequest request, LocalDateTime updatedAt) {
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setUpdatedAt(updatedAt);
        return post;
    }

    public FollowId toFollowId(UUID followerId, UUID followingId) {
        FollowId followId = new FollowId();
        followId.setFollower(followerId);
        followId.setFollowing(followingId);
        return followId;
    }

    public Follow toFollow(User follower, User following, FollowId followId) {
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        follow.setId(followId);
        return follow;
    }

    public LikeId toLikeId(Long postId, UUID userId) {
        LikeId likeId = new LikeId();
        likeId.setPostId(postId);
        likeId.setUserId(userId);
        return likeId;
    }

    public Like toLike(User user, Post post) {
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        return like;
    }
}
