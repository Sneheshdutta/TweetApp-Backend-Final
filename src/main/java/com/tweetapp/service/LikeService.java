package com.tweetapp.service;

import com.tweetapp.exception.TweetAppException;
import com.tweetapp.model.Comments;
import com.tweetapp.model.LikeTable;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.Users;
import com.tweetapp.model.utilityModel.TweetWithLikeComment;
import com.tweetapp.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    public TweetWithLikeComment likeATweet(String username, Long tweetId) throws TweetAppException {
        if(userService.usernameIsEmpty(username))
            throw new TweetAppException("Username doesn't exists");
        if(tweetService.tweetIsEmpty(tweetId))
            throw new TweetAppException("Tweet id doesn't exists");
        likeRepository.save(LikeTable.builder()
                        .tweetId(tweetId).username(username)
                .build());

        List<LikeTable> likeList = getByTweetId(tweetId);
        List<String> userList = likeList.stream().map(LikeTable::getUsername).collect(Collectors.toList());
        List<Users> usersList1 = userService.getAllUsersInList(userList);

        //find bytweetId in comment
        //add all comment to the list
        // assign to the tweetWithLikeComment variable

        List<Comments> commentsList = commentService.getByTweetId(tweetId);

        Tweet tweet = tweetService.getTweetById(tweetId);
        return TweetWithLikeComment.builder()
                .id(tweet.getId())
                .userName(tweet.getUsername())
                .tweets(tweet.getTweet())
                .date(tweet.getDate())
                .likedUsers(usersList1)
                .commentsList(commentsList)
                .build();
    }

    public List<LikeTable> getByTweetId(Long tweetId) {
        return likeRepository.findByTweetId(tweetId);
    }
}
