package com.demo.Exception.Product;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long pid) {
        super("Product with ID " + pid + " not found");
    }
}