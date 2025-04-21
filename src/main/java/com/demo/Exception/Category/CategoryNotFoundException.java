package com.demo.Exception.Category;

public class CategoryNotFoundException extends RuntimeException
{
    public CategoryNotFoundException(Long catId)
    {
        super(String.format("Category with ID %d not found", catId));
    }
}
