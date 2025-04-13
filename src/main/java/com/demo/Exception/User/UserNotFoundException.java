package com.demo.Exception.User;

public class UserNotFoundException extends RuntimeException  
{
    public UserNotFoundException(Long uid)
    {
        super("User with ID " +uid+ " not found");
    }
}