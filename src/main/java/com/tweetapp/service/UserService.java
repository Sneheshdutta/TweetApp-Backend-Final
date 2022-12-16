package com.tweetapp.service;

import com.tweetapp.exception.TweetAppException;
import com.tweetapp.model.Users;
import com.tweetapp.model.utilityModel.ChangePassword;
import com.tweetapp.model.utilityModel.LoginModel;
import com.tweetapp.model.utilityModel.MyUserDetails;
import com.tweetapp.repository.UsersRepository;
import com.tweetapp.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Component("userDetailsImpl")
public class UserService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;


    public Users createUser(Users users) throws TweetAppException {
        if(users == null){
            throw new TweetAppException("User passed is null");
        }
        if(usersRepository.findByEmail(users.getEmail()).isPresent()){
            throw new TweetAppException("Email already exists");
        }
        users.setUsername(users.getFirstName().toLowerCase().substring(0,3)+users.getLastName().toLowerCase().substring(0,3)+users.getContactNumber().substring(0,4));
        if(usersRepository.findByUsername(users.getUsername()).isPresent()){
            throw new TweetAppException("Username already exists");
        }
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        return usersRepository.saveAndFlush(users);
    }

    public Map<String, Object> login(LoginModel user) throws TweetAppException {
        String userEmail = user.getEmail();
        String password = user.getPassword();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, password));
        } catch (BadCredentialsException e) {
            throw new TweetAppException("Incorrect Username or Password", e.getCause());
        }

        final UserDetails userDetails = loadUserByUsername(userEmail);

        final String jwt = jwtUtil.generateToken(userDetails);

        Optional<Users> loggedUser = usersRepository.findByEmail(userEmail);
        Map<String, Object> response = new HashMap<>();
        response.put("username", loggedUser.get().getUsername());
        response.put("jwt", jwt);
        return response;

    }

    public Users updatePassword(ChangePassword cp,String username) throws TweetAppException {
        if(usersRepository.findByUsername(username).isEmpty())
            throw new TweetAppException("Username is not found");

        Users users = usersRepository.findByUsername(username).get();
        if(passwordEncoder.matches(users.getPassword(),cp.getOldPassword()))
            throw new TweetAppException("Old Password mismatch");

        users.setPassword(passwordEncoder.encode(cp.getNewPassword()));
        return usersRepository.saveAndFlush(users);
    }

    public boolean usernameIsEmpty(String username){
        return usersRepository.findByUsername(username).isEmpty();
    }

    public List<Users> getAllUsers(){
        return usersRepository.findAll();
    }

    public List<Users> getUserByRegex(String username) throws TweetAppException {
        return usersRepository.findByUsernameContains(username);
    }

    public Users getUserByEmail(String username) throws TweetAppException {
        if(usernameIsEmpty(username))
            throw new TweetAppException("Username Invalid");
        return usersRepository.findByEmail(username).get();
    }

    public List<Users> getAllUsersInList(List<String> usernames){
        return usersRepository.findByUsernameIn(usernames);
    }


    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Optional<Users> user = usersRepository.findByEmail(userEmail);

        if(user.isEmpty())
            throw new UsernameNotFoundException("Email not present");

        return user.map(MyUserDetails::new).get();
    }
}
