package com.tweetapp.repository;

import com.tweetapp.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comments,Long> {
    List<Comments> findByTweetId(Long tweetId);
    Optional<Comments> findByUsername(String username);
}
