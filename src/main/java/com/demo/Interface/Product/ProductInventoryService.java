package com.demo.Interface.Product;

import java.util.List;
import com.demo.DTO.Product.ProductInventoryDTO;

public interface ProductInventoryService 
{
    ProductInventoryDTO createProductInventory(ProductInventoryDTO productInventoryDTO);            //Method to create a new ProductInventory entry from the provided DTO.
    ProductInventoryDTO getProductInventory(Long siid);                                             //Method to get a specific ProductInventory by its ID (siid).
    List<ProductInventoryDTO> getProductInventories();                                              //Method to get a list of all ProductInventory entries.
    ProductInventoryDTO updateProductInventory(Long siid, ProductInventoryDTO productInventoryDTO); //Method to update an existing ProductInventory entry using the provided DTO.
    void deleteProductInventory(Long siid);                                                         //Method to delete a ProductInventory entry by its ID (siid).
}
