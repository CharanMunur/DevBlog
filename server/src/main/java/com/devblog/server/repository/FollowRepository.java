package com.devblog.server.repository;

import com.devblog.server.model.Follow;
import com.devblog.server.model.FollowId;
import com.devblog.server.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    List<Follow> findByFollowing(User user); // get all followers of a user
    List<Follow> findByFollower(User user); // get all users that user is following
}
