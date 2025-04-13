package com.demo.Exception.User;

public class UsernameNotFoundException extends RuntimeException
{
    public UsernameNotFoundException(String message)
    {
        super(message);
    }
}
