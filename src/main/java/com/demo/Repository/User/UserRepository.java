package com.demo.Repository.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.Entity.User.User;

public interface UserRepository extends JpaRepository<User, Long> 
{

}