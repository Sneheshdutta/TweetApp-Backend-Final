package com.tweetapp.repository;

import com.tweetapp.model.LikeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<LikeTable, Long> {
    List<LikeTable> findByTweetId(Long tweetId);
}
