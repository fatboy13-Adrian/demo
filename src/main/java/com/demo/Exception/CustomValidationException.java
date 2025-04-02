package com.demo.Exception;

public class CustomValidationException extends RuntimeException 
{
    public CustomValidationException(String message) 
    {
        super(message);
    }

    public CustomValidationException(String message, Throwable cause) 
    {
        super(message, cause);
    }
}