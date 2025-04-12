package com.demo.Exception.Cart;

//Custom exception to indicate that a Cart with a given ID was not found in the system
public class CartNotFoundException extends RuntimeException 
{
    //Constructor that accepts the Cart ID and passes a formatted error message to the superclass
    public CartNotFoundException(Long cid) 
    {
        super("Cart with ID " + cid + " not found");
    }
}