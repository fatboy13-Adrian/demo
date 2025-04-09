package com.demo.Exception.Cart;

public class CartItemNotFoundException extends RuntimeException
{
    public CartItemNotFoundException(Long ciid)
    {
        super("Cart Item with ID " +ciid+ " not found");
    }
}