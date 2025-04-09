package com.demo.Exception.Cart;

public class CartNotFoundException extends RuntimeException
{
    public CartNotFoundException(Long cid)
    {
        super("Cart with ID " +cid+ " not found");
    }
}