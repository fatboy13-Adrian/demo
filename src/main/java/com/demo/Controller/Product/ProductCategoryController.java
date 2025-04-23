package com.demo.Controller.Product;

import com.demo.DTO.Product.ProductCategoryDTO;
import com.demo.Exception.Product.ProductCategoryNotFoundException;
import com.demo.Exception.Product.ProductNotFoundException;
import com.demo.Exception.Category.CategoryNotFoundException;
import com.demo.Interface.Product.ProductCategoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-categories")
public class ProductCategoryController {

    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryController.class);
    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @PostMapping
    public ResponseEntity<ProductCategoryDTO> createProductCategory(@RequestBody ProductCategoryDTO dto) {
        try {
            ProductCategoryDTO createdCategory = productCategoryService.createProductCategory(dto);
            return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
        } catch (ProductNotFoundException | CategoryNotFoundException ex) {
            logger.error("Error creating product category: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{pcid}")
    public ResponseEntity<ProductCategoryDTO> getProductCategory(@PathVariable Long pcid) {
        try {
            ProductCategoryDTO productCategory = productCategoryService.getProductCategory(pcid);
            return new ResponseEntity<>(productCategory, HttpStatus.OK);
        } catch (ProductCategoryNotFoundException ex) {
            logger.error("ProductCategory with ID {} not found", pcid);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductCategoryDTO>> getProductCategories() {
        List<ProductCategoryDTO> categories = productCategoryService.getProductCategories();
        if (categories.isEmpty()) {
            logger.warn("No product categories found.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PutMapping("/{pcid}")
    public ResponseEntity<ProductCategoryDTO> updateProductCategory(@PathVariable Long pcid,
                                                                   @RequestBody ProductCategoryDTO dto) {
        try {
            ProductCategoryDTO updatedCategory = productCategoryService.updateProductCategory(pcid, dto);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        } catch (ProductCategoryNotFoundException ex) {
            logger.error("ProductCategory with ID {} not found", pcid);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ProductNotFoundException | CategoryNotFoundException ex) {
            logger.error("Error updating product category: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{pcid}")
    public ResponseEntity<Void> deleteProductCategory(@PathVariable Long pcid) {
        try {
            productCategoryService.deleteProductCategory(pcid);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ProductCategoryNotFoundException ex) {
            logger.error("ProductCategory with ID {} not found", pcid);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
