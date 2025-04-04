package com.demo.Exception.Inventory;

public class InvalidItemInventoryReferenceException extends RuntimeException 
{
    //Constructor that takes the ItemInventory ID (siid) as a parameter
    public InvalidItemInventoryReferenceException(Long siid) 
    {
        //Passes the custom error message to the superclass (RuntimeException)
        super("Cannot delete ItemInventory with ID " + siid + " because it still has valid references.");
    }
}