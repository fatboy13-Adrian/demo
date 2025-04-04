package com.demo.Exception.Item;

public class ItemInventoryNotFoundException extends RuntimeException  
{
    //Constructor that takes the ItemInventory ID as a parameter
    public ItemInventoryNotFoundException(Long siid)
    {
        super("Item inventory with ID" +siid+ " not found");    //The error message includes the item inventory ID that was not found
    }
}