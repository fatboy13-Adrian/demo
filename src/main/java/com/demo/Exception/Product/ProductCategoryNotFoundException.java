package com.demo.Exception.Product;

public class ProductCategoryNotFoundException extends RuntimeException{
    public ProductCategoryNotFoundException(Long pcid)
    {
        super("Product category with ID " +pcid+ " not found");
    }
}
