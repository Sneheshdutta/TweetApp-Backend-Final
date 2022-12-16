package com.tweetapp.controller;

import com.tweetapp.exception.TweetAppException;
//import com.tweetapp.handler.KafkaProducer;
import com.tweetapp.model.Users;
import com.tweetapp.model.utilityModel.ApiResponse;
import com.tweetapp.model.utilityModel.ChangePassword;
import com.tweetapp.model.utilityModel.LoginModel;
import com.tweetapp.service.UserService;
import com.tweetapp.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@Slf4j
@RequestMapping("/api/v1.0/tweets")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;
//    @Autowired
//    KafkaProducer kafkaProducer;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody @Valid Users users) throws TweetAppException {
        Users createdUser = userService.createUser(users);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{loginId}").buildAndExpand(createdUser.getId()).toUri();
        log.info("User created successfully");
        return ResponseEntity.created(uri).body(ApiResponse.builder().status(201).message("User registered successfully").data(createdUser.getUsername()).build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@Valid @RequestBody LoginModel users) throws TweetAppException {
        Map<String, Object> response = userService.login(users);
        log.info("Jwt - "+response.get("jwt"));
        if(response.get("jwt")!=null)
            return ResponseEntity.ok(ApiResponse.builder()
                            .status(200)
                            .message("Login successful")
                            .data(response)
                    .build());
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                        .status(400).message("Login unsuccessful")
                .build());
    }

    @PostMapping("/{username}/forgot")
    public ResponseEntity<ApiResponse> changePassword(@PathVariable String username, @Valid @RequestBody ChangePassword cp)
            throws TweetAppException {
        log.info("Entered changePassword");
//        kafkaProducer.sendMessageToTopic(username); // start kafka
        Users user = userService.updatePassword(cp, username);
        log.info("Password reset successful");
        return ResponseEntity.ok()
                .body(ApiResponse.builder().status(200).message("Password reset successful").data(user).build());

    }

    @GetMapping("/user/search/{username}")
    public ResponseEntity<ApiResponse> searchByUsername(@PathVariable String username) throws TweetAppException {
        List<Users> usersList = userService.getUserByRegex(username);
        return ResponseEntity.ok().body(ApiResponse.builder()
                .status(200).message("Users Found").data(usersList)
                .build());
    }

    @GetMapping("users/all")
    public ResponseEntity<ApiResponse> getAllUsers(){
        return ResponseEntity.ok().body(ApiResponse.builder()
                .status(200).message("Users Found").data(userService.getAllUsers())
                .build());
    }
}
