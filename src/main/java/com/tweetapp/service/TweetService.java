package com.tweetapp.service;

import com.tweetapp.exception.TweetAppException;
import com.tweetapp.model.Comments;
import com.tweetapp.model.LikeTable;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.Users;
import com.tweetapp.model.utilityModel.TweetWithLikeComment;
import com.tweetapp.repository.CommentRepository;
import com.tweetapp.repository.LikeRepository;
import com.tweetapp.repository.TweetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TweetService {
    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    public List<TweetWithLikeComment> getAllTweets(){
        List<TweetWithLikeComment> result = new ArrayList<>();
        List<Tweet> allTweets = tweetRepository.findAll();
        for(Tweet tweet:allTweets) {
            List<LikeTable> likes = likeRepository.findByTweetId(tweet.getId());
            List<Users> userList = userService.getAllUsersInList(likes.stream().map(LikeTable::getUsername).collect(Collectors.toList()));
            List<Comments> comments = commentRepository.findByTweetId(tweet.getId());
            result.add(TweetWithLikeComment.builder()
                    .id(tweet.getId())
                    .userName(tweet.getUsername())
                    .tweets(tweet.getTweet())
                    .date(tweet.getDate())
                    .likedUsers(userList)
                    .commentsList(comments)
                    .build());
        }
        return result;

    }

    public Tweet createATweet(Tweet tweet, String userName) throws TweetAppException {
        if(userService.usernameIsEmpty(userName))
            throw new TweetAppException("Username is not present");
        tweet.setUsername(userName);
        tweet.setDate(new Date());
        return tweetRepository.saveAndFlush(tweet);
    }

    public List<TweetWithLikeComment> getAllTweetsForAUser(String username) throws TweetAppException {
        if(userService.usernameIsEmpty(username)) {
            throw new TweetAppException("Invalid username");
        }
        List<TweetWithLikeComment> result = new ArrayList<>();
        List<Tweet> allTweets = tweetRepository.findByUsername(username);
        for(Tweet tweet:allTweets) {
            List<LikeTable> likes = likeRepository.findByTweetId(tweet.getId());
            List<Users> userList = userService.getAllUsersInList(likes.stream().map(LikeTable::getUsername).collect(Collectors.toList()));
            List<Comments> comments = commentRepository.findByTweetId(tweet.getId());
            result.add(TweetWithLikeComment.builder()
                    .id(tweet.getId())
                    .userName(tweet.getUsername())
                    .tweets(tweet.getTweet())
                    .date(tweet.getDate())
                    .likedUsers(userList)
                    .commentsList(comments)
                    .build());
        }
        return result;
    }

    public boolean tweetIsEmpty(Long tweetId){
        return tweetRepository.findById(tweetId).isEmpty();
    }

    public Tweet updateATweet(String username, Long tweetId, Tweet tweet) throws TweetAppException {
        if(tweetIsEmpty(tweetId))
            throw new TweetAppException("Tweet Id is not present. Please enter a valid tweet Id");
        if(userService.usernameIsEmpty(username))
            throw new TweetAppException("Username is not not valid.");
        Tweet tweetInDb  = tweetRepository.findById(tweetId).get();
        tweetInDb.setTweet(tweet.getTweet());
        return tweetRepository.saveAndFlush(tweetInDb);
    }

    public void deleteTweet(String username, Long tweetId) throws TweetAppException {
        log.info("Inside DeleteTweet "+ username+" "+tweetId);
        if(tweetIsEmpty(tweetId))
            throw new TweetAppException("Tweet Id is not present. Please enter a valid tweet Id");
        if(userService.usernameIsEmpty(username))
            throw new TweetAppException("Username is not not valid.");
        tweetRepository.deleteById(tweetId);
    }

    public Tweet getTweetById(Long tweetId) throws TweetAppException {
        if(tweetIsEmpty(tweetId))
            throw new TweetAppException("Invalid TweetId");
        return tweetRepository.findById(tweetId).get();
    }
}
