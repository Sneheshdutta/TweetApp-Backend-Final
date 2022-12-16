package com.tweetapp.exception;

public class TweetAppException extends RuntimeException{
    public TweetAppException(String message) {
        super(message);
    }
    public TweetAppException(String message, Throwable throwable){
        super(message,throwable);
    }
}
