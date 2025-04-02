package com.demo.Exception.Item;

public class ItemNotFoundException extends RuntimeException 
{
    //Constructor that takes the Item ID as a parameter
    public ItemNotFoundException(Long iid)
    {
        //Calls the constructor of RuntimeException with a custom error message
        super("Item with ID " +iid+ " not found");  //The error message includes the item ID that was not found
    }
}
