package com.tweetapp.controller;

import com.tweetapp.exception.TweetAppException;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.utilityModel.ApiResponse;
import com.tweetapp.model.utilityModel.TweetWithLikeComment;
import com.tweetapp.service.TweetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/api/v1.0/tweets")
@CrossOrigin("*")
@Slf4j
public class TweetController {
    @Autowired
    private TweetService tweetService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllTweets() throws TweetAppException {
        List<TweetWithLikeComment> tweetList = tweetService.getAllTweets();
        log.info(tweetList.toString());
        if(tweetList.isEmpty())
            throw new TweetAppException("There are no tweets available");
        return ResponseEntity.ok(ApiResponse.builder()
                        .status(200).message("Tweets Fetched successfully").data(tweetList)
                .build());
    }

    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse> getAllTweetsForAUser(@PathVariable String username) throws TweetAppException {
        List<TweetWithLikeComment> tweetList = tweetService.getAllTweetsForAUser(username);
        if(tweetList.isEmpty())
            throw new TweetAppException("There are no tweets available");
        return ResponseEntity.ok(ApiResponse.builder()
                .status(200).message("Tweets Fetched successfully for user "+username).data(tweetList)
                .build());
    }

    @PostMapping("/{username}/add")
    public ResponseEntity<ApiResponse> postTweetForAUser(@Valid @RequestBody Tweet tweet, @PathVariable String username) throws TweetAppException {
        Tweet createdTweet = tweetService.createATweet(tweet,username);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{tweetId}").buildAndExpand(createdTweet.getId()).toUri();
        return ResponseEntity.created(uri).body(ApiResponse.builder()
                .status(200).message("Tweet created successfully for user "+username).data(createdTweet)
                .build());
    }

    @PutMapping("{username}/update/{tweetId}")
    public ResponseEntity<ApiResponse> updateTweet(@Valid @RequestBody Tweet tweet, @PathVariable String username, @PathVariable Long tweetId) throws TweetAppException {
        Tweet tweet1 = tweetService.updateATweet(username,tweetId,tweet);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(200).message("Tweets Updated successfully for user "+username).data(tweet1)
                .build());
    }

    @DeleteMapping("{username}/delete/{tweetId}")
    public ResponseEntity<ApiResponse> deleteTweet(@PathVariable String username, @PathVariable Long tweetId) throws TweetAppException {
        tweetService.deleteTweet(username,tweetId);
        return ResponseEntity.ok(ApiResponse.builder()
                .status(200).message("Tweets deleted successfully for user "+username+" with tweet id "+tweetId)
                .build());
    }



}
