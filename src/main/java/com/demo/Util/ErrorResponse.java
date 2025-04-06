package com.demo.Util;

public class ErrorResponse 
{
    private String error;   //Instance variable to store the error message

    //Constructor that initializes the error message
    public ErrorResponse(String error) 
    {
        this.error = error;     //Assigning the provided error message to the instance variable
    }

    //Getter method to retrieve the error message
    public String getError() 
    {
        return error;           //Returns the error message stored in the instance variable
    }

    //Setter method to update the error message
    public void setError(String error) 
    {
        this.error = error;     //Sets the new error message to the instance variable
    }
}