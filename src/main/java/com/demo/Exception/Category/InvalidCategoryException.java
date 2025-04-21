package com.demo.Exception.Category;

public class InvalidCategoryException extends RuntimeException 
{
    public InvalidCategoryException(String message)
    {
        super(message);
    }
}
