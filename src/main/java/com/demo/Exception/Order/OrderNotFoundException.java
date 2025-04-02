package com.demo.Exception.Order;

public class OrderNotFoundException extends RuntimeException 
{
    //Constructor that takes the order ID as a parameter
    public OrderNotFoundException(Long oid) 
    {
        //Calls the constructor of RuntimeException with a custom error message
        super("Order with ID " + oid + " not found");   //The error message includes the order ID that was not found
    }
}