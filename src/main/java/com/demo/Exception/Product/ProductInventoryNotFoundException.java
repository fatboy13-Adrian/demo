package com.demo.Exception.Product;

public class ProductInventoryNotFoundException extends RuntimeException 
{
    public ProductInventoryNotFoundException(Long psid)
    {
        super("Product Inventory with ID " +psid+ " not found");
    }
}
