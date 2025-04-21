package com.demo.Controller.Product;

import com.demo.DTO.Product.ProductDTO;
import com.demo.Exception.Product.ProductNotFoundException;
import com.demo.Interface.Product.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        try {
            ProductDTO createdProduct = productService.createProduct(productDTO);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{pid}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long pid) {
        try {
            ProductDTO productDTO = productService.getProduct(pid);
            return ResponseEntity.ok(productDTO);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts() {
        List<ProductDTO> products = productService.getProducts();
        return ResponseEntity.ok(products);
    }

    @PatchMapping("/{pid}")
    public ResponseEntity<ProductDTO> partialUpdateProduct(@PathVariable Long pid, @RequestBody ProductDTO productDTO) {
        try {
            ProductDTO updatedProduct = productService.partialUpdateProduct(pid, productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{pid}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long pid) {
        try {
            productService.deleteProduct(pid);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
} 