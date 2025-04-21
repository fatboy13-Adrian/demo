package com.demo.Controller.Product;

import com.demo.DTO.Product.ProductDTO;
import com.demo.Exception.Product.ProductNotFoundException;
import com.demo.Interface.Product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductDTO productDTO;

    @BeforeEach
    public void setUp() {
        productDTO = new ProductDTO(1L, "Laptop", new BigDecimal("1200.00"));
    }

    @Test
    public void testCreateProduct() {
        when(productService.createProduct(any(ProductDTO.class))).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.createProduct(productDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Laptop", response.getBody().getProductName());
        assertEquals(new BigDecimal("1200.00"), response.getBody().getUnitPrice());
    }

    @Test
    public void testCreateProductBadRequest() {
        when(productService.createProduct(any(ProductDTO.class)))
                .thenThrow(new RuntimeException("Error creating product"));

        ResponseEntity<ProductDTO> response = productController.createProduct(productDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetProduct() {
        when(productService.getProduct(1L)).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = productController.getProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Laptop", response.getBody().getProductName());
    }

    @Test
    public void testGetProductNotFound() {
        when(productService.getProduct(1L)).thenThrow(new ProductNotFoundException(1L));

        ResponseEntity<ProductDTO> response = productController.getProduct(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetProducts() {
        List<ProductDTO> products = List.of(productDTO);
        when(productService.getProducts()).thenReturn(products);

        ResponseEntity<List<ProductDTO>> response = productController.getProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testPartialUpdateProduct() {
        ProductDTO updatedProductDTO = new ProductDTO(1L, "Gaming Laptop", new BigDecimal("1500.00"));
        when(productService.partialUpdateProduct(1L, updatedProductDTO)).thenReturn(updatedProductDTO);

        ResponseEntity<ProductDTO> response = productController.partialUpdateProduct(1L, updatedProductDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Gaming Laptop", response.getBody().getProductName());
        assertEquals(new BigDecimal("1500.00"), response.getBody().getUnitPrice());
    }

    @Test
    public void testPartialUpdateProductNotFound() {
        ProductDTO updatedProductDTO = new ProductDTO(1L, "Gaming Laptop", new BigDecimal("1500.00"));
        when(productService.partialUpdateProduct(1L, updatedProductDTO)).thenThrow(new ProductNotFoundException(1L));

        ResponseEntity<ProductDTO> response = productController.partialUpdateProduct(1L, updatedProductDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteProduct() {
        doNothing().when(productService).deleteProduct(1L);

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteProductNotFound() {
        doThrow(new ProductNotFoundException(1L)).when(productService).deleteProduct(1L);

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
