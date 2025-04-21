package com.demo.Service.Product;

import com.demo.DTO.Product.ProductInventoryDTO;
import com.demo.Entity.Inventory.Inventory;
import com.demo.Entity.Product.Product;
import com.demo.Entity.Product.ProductInventory;
import com.demo.Exception.Product.ProductInventoryNotFoundException;
import com.demo.Interface.Product.ProductInventoryService;
import com.demo.Repository.Inventory.InventoryRepository;
import com.demo.Repository.Product.ProductInventoryRepository;
import com.demo.Repository.Product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional //Ensures the service method runs within a transaction context
public class ProductInventoryServiceImpl implements ProductInventoryService 
{
    private final ProductInventoryRepository productInventoryRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    //Constructor for dependency injection of repositories
    public ProductInventoryServiceImpl(ProductInventoryRepository productInventoryRepository, ProductRepository productRepository, InventoryRepository inventoryRepository) 
    {
        this.productInventoryRepository = productInventoryRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    //Converts a ProductInventory entity to a DTO (Data Transfer Object)
    private ProductInventoryDTO toDTO(ProductInventory productInventory) 
    {
        return ProductInventoryDTO.builder().psid(productInventory.getPsid()).pid(productInventory.getPid().getPid()).sid(productInventory.getSid().getSid())
        .build();
    }

    //Converts a DTO to a ProductInventory entity
    private ProductInventory toEntity(ProductInventoryDTO dto) 
    {
        //Find the product using its ID from the repository or throw an exception if not found
        Product product = productRepository.findById(dto.getPid()).orElseThrow(() -> new ProductInventoryNotFoundException(dto.getPid()));
        
        //Find the inventory using its ID from the repository or throw an exception if not found
        Inventory inventory = inventoryRepository.findById(dto.getSid()).orElseThrow(() -> new ProductInventoryNotFoundException(dto.getSid()));

        //Return a new ProductInventory entity built using the DTO values
        return ProductInventory.builder().psid(dto.getPsid()).pid(product).sid(inventory).build();
    }

    @Override
    public ProductInventoryDTO createProductInventory(ProductInventoryDTO dto) 
    {
        ProductInventory entity = toEntity(dto);                           //Convert DTO to entity for saving
        ProductInventory saved = productInventoryRepository.save(entity);     //Save the entity using the repository
        return toDTO(saved);                                            //Convert the saved entity back to DTO and return it
    }

    @Override
    public ProductInventoryDTO getProductInventory(Long psid) 
    {
        //Retrieve the product inventory by psid from the repository or throw exception if not found
        ProductInventory productInventory = productInventoryRepository.findById(psid).orElseThrow(() -> new ProductInventoryNotFoundException(psid));
        return toDTO(productInventory);    //Convert the entity to DTO and return it
    }

    @Override
    public List<ProductInventoryDTO> getProductInventories() 
    {
        //Retrieve all product inventories from the repository
        return productInventoryRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList()); 
    }

    @Override
    public ProductInventoryDTO updateProductInventory(Long psid, ProductInventoryDTO dto) 
    {
        //Check if the product inventory exists by its psid, if not throw an exception
        productInventoryRepository.findById(psid).orElseThrow(() -> new ProductInventoryNotFoundException(psid));

        //Update the DTO to ensure psid is included for the update
        dto = ProductInventoryDTO.builder().psid(psid).pid(dto.getPid()).sid(dto.getSid()).build();

        ProductInventory updated = toEntity(dto);                          //Convert the updated DTO back to an entity for saving
        ProductInventory saved = productInventoryRepository.save(updated);    //Save the updated entity using the repository
        return toDTO(saved);                                            //Convert the saved entity back to DTO and return it
    }

    @Override
    public void deleteProductInventory(Long psid) 
    {
        //Retrieve the product inventory by psid, or throw an exception if not found
        ProductInventory productInventory = productInventoryRepository.findById(psid).orElseThrow(() -> new ProductInventoryNotFoundException(psid));
        
        //Delete the product inventory from the repository
        productInventoryRepository.delete(productInventory);
    }
}
