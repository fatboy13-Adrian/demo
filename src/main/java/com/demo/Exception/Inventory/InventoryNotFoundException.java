package com.demo.Exception.Inventory;

public class InventoryNotFoundException extends RuntimeException
{
    public InventoryNotFoundException(Long sid)
    {
        super("Inventory with ID " +sid+ " not found");
    }
}