package com.demo.Util;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter             //Lombok annotation to generate getter methods for all fields
@AllArgsConstructor //Lombok annotation to generate a constructor with all arguments
@NoArgsConstructor  //Lombok annotation to generate a no-args constructor (needed for JSON serialization)
public class ErrorResponse 
{
    private String message; //Error message describing the issue
    private Long pid;       //Payment ID related to the error (if applicable)
}