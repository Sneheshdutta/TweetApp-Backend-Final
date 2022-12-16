package com.tweetapp.controller;

import com.tweetapp.exception.TweetAppException;
import com.tweetapp.model.utilityModel.ApiResponse;
import com.tweetapp.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1.0/tweets")
@CrossOrigin("*")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PutMapping("/{username}/like/{tweetId}")
    public ResponseEntity<ApiResponse> likeATweet(@PathVariable String username, @PathVariable Long tweetId) throws TweetAppException {
        return ResponseEntity.ok(ApiResponse.builder()
                        .status(200).message("Liked the tweet")
                .data(likeService.likeATweet(username,tweetId))
                        .build());
    }
}
