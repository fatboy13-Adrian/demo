package com.demo.Controller.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.demo.DTO.Product.ProductInventoryDTO;
import com.demo.Exception.Product.ProductInventoryNotFoundException;
import com.demo.Interface.Product.ProductInventoryService;

import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController
@RequestMapping("/product-inventories")
@Slf4j
public class ProductInventoryController 
{
    private final ProductInventoryService productInventoryService;

    public ProductInventoryController(ProductInventoryService productInventoryService) 
    {
        this.productInventoryService = productInventoryService;
    }

    @PostMapping
    public ResponseEntity<ProductInventoryDTO> createProductInventory(@RequestBody ProductInventoryDTO productInventoryDTO) 
    {
        try 
        {
            log.info("Creating ProductInventory with details: {}", productInventoryDTO);
            ProductInventoryDTO createdProductInventory = productInventoryService.createProductInventory(productInventoryDTO);
            return new ResponseEntity<>(createdProductInventory, HttpStatus.CREATED);
        } 
        catch(ProductInventoryNotFoundException e) 
        {
            log.error("Error creating ProductInventory: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{psid}")
    public ResponseEntity<ProductInventoryDTO> getProductInventory(@PathVariable Long psid) 
    {
        try 
        {
            log.info("Retrieving ProductInventory with psid: {}", psid);
            ProductInventoryDTO productInventoryDTO = productInventoryService.getProductInventory(psid);
            return new ResponseEntity<>(productInventoryDTO, HttpStatus.OK);
        } 
        catch(ProductInventoryNotFoundException e) 
        {
            log.error("ProductInventory with psid {} not found: {}", psid, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductInventoryDTO>> getAllProductInventories() 
    {
        log.info("Request to retrieve all ProductInventories");
        List<ProductInventoryDTO> productInventoryDTOList = productInventoryService.getProductInventories();
        return new ResponseEntity<>(productInventoryDTOList, HttpStatus.OK);
    }

    @PutMapping("/{psid}")
    public ResponseEntity<ProductInventoryDTO> updateProductInventory(@PathVariable Long psid, @RequestBody ProductInventoryDTO productInventoryDTO) 
    {
        try 
        {
            log.info("Updating ProductInventory with psid: {} and new details: {}", psid, productInventoryDTO);
            ProductInventoryDTO updatedProductInventory = productInventoryService.updateProductInventory(psid, productInventoryDTO);
            return new ResponseEntity<>(updatedProductInventory, HttpStatus.OK);
        } 
        catch(ProductInventoryNotFoundException e) 
        {
            log.error("ProductInventory with psid {} not found for update: {}", psid, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{psid}")
    public ResponseEntity<Void> deleteProductInventory(@PathVariable Long psid) 
    {
        try 
        {
            log.info("Deleting ProductInventory with psid: {}", psid);
            productInventoryService.deleteProductInventory(psid);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } 
        catch(ProductInventoryNotFoundException e) 
        {
            log.error("ProductInventory with psid {} not found for deletion: {}", psid, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}