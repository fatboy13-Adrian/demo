package com.demo.Exception;

public class DeletedIdNotFoundException extends RuntimeException 
{
    //Constructor that takes a Long id as input, it passes a message to super class with details about the missing deleted ID.
    public DeletedIdNotFoundException(Long id) 
    {
        super("Deleted ID with ID " + id + " not found");   //Constructs the exception message dynamically, inserting the ID into the message.
    }
}