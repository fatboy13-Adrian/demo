package com.demo.Repository.User;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.Entity.User.User;

public interface UserRepository extends JpaRepository<User, Long> 
{
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}