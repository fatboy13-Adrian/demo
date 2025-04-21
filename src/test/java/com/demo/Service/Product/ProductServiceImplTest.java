package com.demo.Service.Product;

import com.demo.DTO.Product.ProductDTO;
import com.demo.Entity.Product.Product;
import com.demo.Exception.Product.ProductNotFoundException;
import com.demo.Repository.Product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest 
{
    @Mock
    private ProductRepository productRepository; //Mocked ProductRepository

    @InjectMocks
    private ProductServiceImpl productService; //The service we are testing

    private ProductDTO productDTO;

    @BeforeEach
    public void setUp() 
    {
        //Initialize a sample product DTO for testing
        productDTO = new ProductDTO(1L, "Laptop", new BigDecimal("1200.00"));
    }

    @Test   //Positive Test Case for createProduct
    public void testCreateProduct() 
    {
        //Given
        Product product = new Product(1L, "Laptop", new BigDecimal("1200.00"));
        when(productRepository.save(any(Product.class))).thenReturn(product);                    //Mocking the save operation

        //When
        ProductDTO createdProduct = productService.createProduct(productDTO);

        //Then
        assertNotNull(createdProduct);
        assertEquals("Laptop", createdProduct.getProductName());
        assertEquals(new BigDecimal("1200.00"), createdProduct.getUnitPrice());
        verify(productRepository, times(1)).save(any(Product.class)); //Verifying that save was called once
    }

    @Test   //Negative Test Case for getProduct (Product Not Found)
    public void testGetProductNotFound() 
    {
        //Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());         //Mocking not found scenario

        //When & Then
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> 
        {
            productService.getProduct(1L);
        });

        assertEquals("Product with ID 1 not found", exception.getMessage());   //Verifying the exception message
    }

    @Test   //Positive Test Case for getProduct
    public void testGetProduct() 
    {
        //Given
        Product product = new Product(1L, "Laptop", new BigDecimal("1200.00"));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));    //Mocking findById

        //When
        ProductDTO foundProduct = productService.getProduct(1L);

        //Then
        assertNotNull(foundProduct);
        assertEquals("Laptop", foundProduct.getProductName());
        assertEquals(new BigDecimal("1200.00"), foundProduct.getUnitPrice());
    }

    @Test   //Positive Test Case for partialUpdateProduct
    public void testPartialUpdateProduct() 
    {
        //Given
        Product existingProduct = new Product(1L, "Laptop", new BigDecimal("1200.00"));
        ProductDTO updatedProductDTO = new ProductDTO(1L, "Gaming Laptop", new BigDecimal("1500.00"));
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        //When
        ProductDTO updatedProduct = productService.partialUpdateProduct(1L, updatedProductDTO);

        //Then
        assertNotNull(updatedProduct);
        assertEquals("Gaming Laptop", updatedProduct.getProductName());           //Product name should be updated
        assertEquals(new BigDecimal("1500.00"), updatedProduct.getUnitPrice());    //Unit price should be updated
    }

    @Test   //Negative Test Case for partialUpdateProduct (Product Not Found)
    public void testPartialUpdateProductNotFound() 
    {
        //Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty()); //Mocking not found scenario

        //When & Then
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> 
        {
            productService.partialUpdateProduct(1L, productDTO);
        });

        assertEquals("Product with ID 1 not found", exception.getMessage()); //Verifying the exception message
    }

    @Test   //Positive Test Case for deleteProduct
    public void testDeleteProduct() 
    {
        //Given
        Product product = new Product(1L, "Laptop", new BigDecimal("1200.00"));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        //When
        productService.deleteProduct(1L);

        //Then
        verify(productRepository, times(1)).delete(product); //Verifying that delete was called once
    }

    @Test   //Negative Test Case for deleteProduct (Product Not Found)
    public void testDeleteProductNotFound() 
    {
        //Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty()); //Mocking not found scenario

        //When & Then
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> 
        {
            productService.deleteProduct(1L);
        });

        assertEquals("Product with ID 1 not found", exception.getMessage()); //Verifying the exception message
    }
}
