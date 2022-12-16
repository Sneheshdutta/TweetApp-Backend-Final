package com.tweetapp.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class LoginException extends UsernameNotFoundException {
    public LoginException(String incorrect_authentication_structure) {
        super(incorrect_authentication_structure);
    }
}
