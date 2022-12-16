package com.tweetapp.service;

import com.tweetapp.exception.TweetAppException;
import com.tweetapp.model.Comments;
import com.tweetapp.model.LikeTable;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.Users;
import com.tweetapp.model.utilityModel.TweetWithLikeComment;
import com.tweetapp.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private UserService userService;

    @Lazy
    @Autowired
    private LikeService likeService;

    public TweetWithLikeComment commentATweet(Comments comments, String username, Long tweetId) throws TweetAppException {
        if(userService.usernameIsEmpty(username))
            throw new TweetAppException("Username doesn't exists");
        if(tweetService.tweetIsEmpty(tweetId))
            throw new TweetAppException("Tweet id doesn't exists");
        comments.setUsername(username);
        comments.setTweetId(tweetId);
        comments.setDate(new Date());
        commentRepository.saveAndFlush(comments);
        //getTweet
        Tweet tweet = tweetService.getTweetById(tweetId);
        //getLikes
        List<LikeTable> likeList = likeService.getByTweetId(tweetId);
        List<String> userList = likeList.stream().map(LikeTable::getUsername).collect(Collectors.toList());
        List<Users> usersList1 = userService.getAllUsersInList(userList);
        //getComments
        List<Comments> commentList = getByTweetId(tweetId);
        return TweetWithLikeComment.builder()
                .id(tweet.getId())
                .userName(tweet.getUsername())
                .tweets(tweet.getTweet())
                .date(tweet.getDate())
                .likedUsers(usersList1)
                .commentsList(commentList)
                .build();
    }

    public List<Comments> getByTweetId(Long tweetId) {
        return commentRepository.findByTweetId(tweetId);
    }
}
