package com.tweetapp.repository;

import com.tweetapp.model.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TweetRepository extends JpaRepository<Tweet,Long> {
    Optional<Tweet> findById(Long id);

    List<Tweet> findByUsername(String username);
}
