package com.demo.Exception.Order;

//Custom exception class for handling situations where an OrderProduct is not found
public class OrderProductNotFoundException extends RuntimeException 
{
    //Constructor that accepts the OrderProduct ID (opid) and constructs the error message
    public OrderProductNotFoundException(Long opid) 
    {
        super("Order product with ID " + opid + " not found"); //Calls the constructor of the parent RuntimeException with a custom message
    }
}
