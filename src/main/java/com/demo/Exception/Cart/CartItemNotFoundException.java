package com.demo.Exception.Cart;

//Custom runtime exception thrown when a Cart Item with a specific ID is not found
public class CartItemNotFoundException extends RuntimeException 
{
    //Constructor that takes the missing Cart Item ID and constructs a detailed error message
    public CartItemNotFoundException(Long ciid) 
    {
        super("Cart Item with ID " + ciid + " not found");
    }
}