package com.demo.Exception.Order;

//Custom exception class for handling situations where an OrderItem is not found
public class OrderItemNotFoundException extends RuntimeException 
{
    //Constructor that accepts the OrderItem ID (oiid) and constructs the error message
    public OrderItemNotFoundException(Long oiid) 
    {
        super("Order item with ID " + oiid + " not found"); //Calls the constructor of the parent RuntimeException with a custom message
    }
}