package com.tweetapp.repository;

import com.tweetapp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {

    Optional<Users> findById(String id);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByUsername(String username);

    List<Users> findByUsernameContains(String username);

    List<Users> findByUsernameIn(List<String> usernames);

    //List<Users> searchByRegex(String str);
}
