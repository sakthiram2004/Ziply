package com.sr.Ziply.repository;


import com.sr.Ziply.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String username);

    boolean existsByEmail(String email);
}
